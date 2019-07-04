package com.doctorwork.sword.gateway.loadbalance;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.discovery.IDiscoveryRepository;
import com.doctorwork.sword.gateway.discovery.ServiceWrapper;
import com.doctorwork.sword.gateway.loadbalance.server.CompositiveServerList;
import com.doctorwork.sword.gateway.loadbalance.server.DataBaseServerList;
import com.doctorwork.sword.gateway.loadbalance.server.ZookeeperServerList;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:27 2019/7/2
 * @Modified By:
 */
public class CustomerLoadBalanceClient extends AbstractLoadBalanceClient {

    private final Map<String, ILoadBalancer> loadBalancerMap = new ConcurrentHashMap<>();


    private GatewayLoadBalanceService gatewayLoadBalanceService;
    private IDiscoveryRepository iDiscoveryRepository;

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
        ILoadBalancer loadBalancer = loadBalancerMap.get(serviceId);
        if (loadBalancer == null) {
            synchronized (loadBalancerMap) {
                LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(serviceId);
                if (loadbalanceInfo == null)
                    return null;
                loadBalancer = loadBalancerMap.get(serviceId);
                if (loadBalancer != null) {
                    return loadBalancer;
                }
                ServerList serverList;
                if (loadbalanceInfo.getDscrEnable() == null || loadbalanceInfo.getDscrEnable().equals(0)) {
                    serverList = new DataBaseServerList(serviceId, gatewayLoadBalanceService);
                } else {
                    ServiceWrapper wrapper = iDiscoveryRepository.serviceWrapper(serviceId);
                    serverList = new CompositiveServerList(serviceId, new DataBaseServerList(serviceId, gatewayLoadBalanceService),
                            new ZookeeperServerList(serviceId, wrapper));
                }
                DynamicLoadBalancer dynamicLoadBalancer = new DynamicLoadBalancer(serverList);
                dynamicLoadBalancer.init(loadbalanceInfo);
                loadBalancerMap.putIfAbsent(serviceId, dynamicLoadBalancer);
            }
            return loadBalancerMap.get(serviceId);
        }
        return loadBalancer;
    }
}
