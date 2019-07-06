package com.doctorwork.sword.gateway.loadbalance;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.discovery.IDiscoveryRepository;
import com.doctorwork.sword.gateway.discovery.ServiceWrapper;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
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
    public void loadBalanceInit(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        long stamp = stampedLock.writeLock();
        try {
            LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(lbMark);
            if (loadbalanceInfo == null)
                return;
            ILoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
            if (loadBalancer != null) {
                return;
            }
            ServerList serverList;
            Boolean dscrEnable = loadbalanceInfo.getDscrEnable() != null && loadbalanceInfo.getDscrEnable().equals(1);
            if (dscrEnable) {
                serverList = new CompositiveServerList(lbMark, new DataBaseServerList(lbMark, gatewayLoadBalanceService));
            } else {
                ServiceWrapper wrapper = iDiscoveryRepository.serviceWrapper(lbMark);
                serverList = new CompositiveServerList(lbMark, true, new DataBaseServerList(lbMark, gatewayLoadBalanceService),
                        new ZookeeperServerList(lbMark, wrapper));
            }
            DynamicLoadBalancer dynamicLoadBalancer = new DynamicLoadBalancer(serverList);
            dynamicLoadBalancer.init(loadbalanceInfo);
            loadBalancerMap.putIfAbsent(lbMark, dynamicLoadBalancer);
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    @Override
    public void loadBalanceDelete(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        ILoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
        if (loadBalancer == null) {
            return;
        }
        if (loadBalancer instanceof BaseLoadBalancer) {
            BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) loadBalancer;
            baseLoadBalancer.shutdown();
        }
        loadBalancerMap.remove(lbMark);
    }

    @Override
    public void loadBalancePingLoad(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        ILoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
        if (loadBalancer == null) {
            return;
        }
        LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(lbMark);
        if (loadbalanceInfo == null)
            return;
        if (loadBalancer instanceof BaseLoadBalancer) {
            DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
            dynamicLoadBalancer.reloadPing(loadbalanceInfo);
        }
    }

    @Override
    public void loadBalanceRuleLoad(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        ILoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
        if (loadBalancer == null) {
            return;
        }
        LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(lbMark);
        if (loadbalanceInfo == null)
            return;
        if (loadBalancer instanceof BaseLoadBalancer) {
            DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
            dynamicLoadBalancer.reloadRule(loadbalanceInfo);
        }
    }

    @Override
    public void loadBalanceAutoRefreshLoad(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        ILoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
        if (loadBalancer == null) {
            return;
        }
        LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(lbMark);
        if (loadbalanceInfo == null)
            return;
        if (loadBalancer instanceof DynamicLoadBalancer) {
            DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
            dynamicLoadBalancer.reloadAutoRefresh(loadbalanceInfo);
        }
    }

    @Override
    public void loadBalanceAutoRefreshShutdown(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        ILoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
        if (loadBalancer == null) {
            return;
        }
        if (loadBalancer instanceof DynamicLoadBalancer) {
            DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
            dynamicLoadBalancer.stopServerListRefreshing();
        }
    }

    @Override
    public void loadBalanceDiscoveryLoad(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(lbMark);
        if (loadbalanceInfo == null)
            return;
        ILoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
        if (loadBalancer == null) {
            return;
        }
        long stamp = stampedLock.writeLock();
        try {
            if (loadBalancer instanceof DynamicLoadBalancer) {
                DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
                ServerList oldServerList = dynamicLoadBalancer.getServerListImpl();
                Boolean dscrEnable = loadbalanceInfo.getDscrEnable() != null && loadbalanceInfo.getDscrEnable().equals(1);
                if (oldServerList == null) {
                    logger.error("no serverlist for {}", lbMark);
                    return;
                } else {
                    CompositiveServerList serverList = (CompositiveServerList) oldServerList;
                    if (!dscrEnable) {
                        serverList.discoveryReload(false, new DataBaseServerList(lbMark, gatewayLoadBalanceService), null);
                    } else {
                        ServiceWrapper wrapper = iDiscoveryRepository.serviceWrapper(lbMark);
                        serverList.discoveryReload(true, new DataBaseServerList(lbMark, gatewayLoadBalanceService), new ZookeeperServerList(lbMark, wrapper));
                    }
                }
            }
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }
}
