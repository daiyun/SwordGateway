package com.doctorwork.sword.gateway.loadbalance;

import com.netflix.loadbalancer.Server;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.ServiceCacheListener;

import java.util.List;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:33 2019/7/2
 * @Modified By:
 */
public class LoadBalanceServerCacheListener implements ServiceCacheListener {

    private ServiceCache<?> serviceCache;
    private DynamicLoadBalancer dynamicLoadBalancer;

    public LoadBalanceServerCacheListener(ServiceCache<?> serviceCache, DynamicLoadBalancer dynamicLoadBalancer) {
        this.serviceCache = serviceCache;
        this.dynamicLoadBalancer = dynamicLoadBalancer;
    }

    @Override
    public void cacheChanged() {
        List<Server> servers = dynamicLoadBalancer.getReachableServers();
        for (Server server : servers) {
            for (ServiceInstance<?> instance : serviceCache.getInstances()) {

            }
        }
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {

    }
}
