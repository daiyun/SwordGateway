package com.doctorwork.sword.gateway.loadbalance;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.discovery.IDiscoveryRepository;
import com.doctorwork.sword.gateway.discovery.ServiceWrapper;
import com.doctorwork.sword.gateway.loadbalance.server.CompositiveServerList;
import com.doctorwork.sword.gateway.loadbalance.server.DataBaseServerList;
import com.doctorwork.sword.gateway.loadbalance.server.ZookeeperServerList;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:27 2019/7/2
 * @Modified By:
 */
public class CustomerLoadBalanceClient extends AbstractLoadBalanceClient implements ILoadBalanceClientManagerApi {

    private final Map<String, ILoadBalancer> loadBalancerMap = new ConcurrentHashMap<>();


    private GatewayLoadBalanceService gatewayLoadBalanceService;
    private IDiscoveryRepository iDiscoveryRepository;

    private final StampedLock stampedLock = new StampedLock();

    public CustomerLoadBalanceClient(GatewayLoadBalanceService gatewayLoadBalanceService, IDiscoveryRepository iDiscoveryRepository) {
        this.gatewayLoadBalanceService = gatewayLoadBalanceService;
        this.iDiscoveryRepository = iDiscoveryRepository;
    }

    @Override
    protected Server getServer(String serviceId) {
        ILoadBalancer loadBalancer = this.getLoadBalancer(serviceId);

        return loadBalancer == null ? null : loadBalancer.chooseServer(serviceId);
    }

    @Override
    protected ILoadBalancer getLoadBalancer(String serviceId) {
        long stamp = stampedLock.tryOptimisticRead();
        ILoadBalancer loadBalancer = loadBalancerMap.get(serviceId);
        if (loadBalancer == null) {
            this.loadBalanceInit(serviceId);
        }
        if (!stampedLock.validate(stamp)) {
            stamp = stampedLock.readLock();
            try {
                loadBalancer = loadBalancerMap.get(serviceId);
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        return loadBalancer;
    }

    @Override
    public void loadBalanceInit(String serviceId) {
        long stamp = stampedLock.writeLock();
        try {
            LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(serviceId);
            if (loadbalanceInfo == null)
                return;
            ILoadBalancer loadBalancer = loadBalancerMap.get(serviceId);
            if (loadBalancer != null) {
                return;
            }
            ServerList serverList;
            Boolean dscrEnable = loadbalanceInfo.getDscrEnable() != null && loadbalanceInfo.getDscrEnable().equals(1);
            if (dscrEnable) {
                serverList = new CompositiveServerList(serviceId, new DataBaseServerList(serviceId, gatewayLoadBalanceService));
            } else {
                ServiceWrapper wrapper = iDiscoveryRepository.serviceWrapper(serviceId);
                serverList = new CompositiveServerList(serviceId, true, new DataBaseServerList(serviceId, gatewayLoadBalanceService),
                        new ZookeeperServerList(serviceId, wrapper));
            }
            DynamicLoadBalancer dynamicLoadBalancer = new DynamicLoadBalancer(serverList);
            dynamicLoadBalancer.init(loadbalanceInfo);
            loadBalancerMap.putIfAbsent(serviceId, dynamicLoadBalancer);
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    @Override
    public void loadBalanceDelete(String serviceId) {
        ILoadBalancer loadBalancer = loadBalancerMap.get(serviceId);
        if (loadBalancer == null) {
            return;
        }
        long stamp = stampedLock.writeLock();
        try {
            if (loadBalancer instanceof BaseLoadBalancer) {
                BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) loadBalancer;
                baseLoadBalancer.shutdown();
            }
            loadBalancerMap.remove(serviceId);
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    @Override
    public void loadBalancePingLoad(String serviceId) {
        ILoadBalancer loadBalancer = loadBalancerMap.get(serviceId);
        if (loadBalancer == null) {
            return;
        }
        long stamp = stampedLock.writeLock();
        try {
            LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(serviceId);
            if (loadbalanceInfo == null)
                return;
            if (loadBalancer instanceof BaseLoadBalancer) {
                DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
                dynamicLoadBalancer.reloadPing(loadbalanceInfo);
            }
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    @Override
    public void loadBalanceRuleLoad(String serviceId) {

    }

    @Override
    public void loadBalanceDiscoveryLoad(String serviceId) {

    }
}
