package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.loadbalance.AbstractLoadBalanceClient;
import com.doctorwork.sword.gateway.loadbalance.DataBaseServerList;
import com.doctorwork.sword.gateway.loadbalance.DynamicLoadBalancer;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:03 2019/6/18
 * @Modified By:
 */
@Configuration
public class LoadBalancerClientAutoConfiguration {

    private static Map<String, ILoadBalancer> loadBalancerMap = new ConcurrentHashMap<>();
    private static final String LOCK_LB = "LOCK_FOR_LOADBALANCE----";


    @Bean
    @ConditionalOnMissingBean({LoadBalancerClientFilter.class})
    public LoadBalancerClientFilter loadBalancerClientFilter(LoadBalancerClient client) {
        return new LoadBalancerClientFilter(client);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadBalancerClient loadBalancerClient(GatewayLoadBalanceService gatewayLoadBalanceService) {
        return new AbstractLoadBalanceClient() {
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
                            return loadBalancer;
                        loadBalancer = loadBalancerMap.get(serviceId);
                        if (loadBalancer != null) {
                            return loadBalancer;
                        }
                        DataBaseServerList serverList = new DataBaseServerList(gatewayLoadBalanceService, serviceId);
                        DynamicLoadBalancer dynamicLoadBalancer = new DynamicLoadBalancer(serverList);
                        dynamicLoadBalancer.init(loadbalanceInfo);
                        loadBalancerMap.putIfAbsent(serviceId, dynamicLoadBalancer);
                    }
                    return loadBalancerMap.get(serviceId);
                }
                return loadBalancer;
            }
        };
    }
}
