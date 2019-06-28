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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:03 2019/6/18
 * @Modified By:
 */
@Configuration
public class LoadBalancerClientAutoConfiguration {

    private static ConcurrentHashMap<String, ILoadBalancer> loadBalancerConcurrentHashMap = new ConcurrentHashMap<>();

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
                ILoadBalancer loadBalancer = loadBalancerConcurrentHashMap.get(serviceId);
                if (loadBalancer == null) {
                    LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(serviceId);
                    if (loadbalanceInfo == null)
                        return loadBalancer;
                    DataBaseServerList serverList = new DataBaseServerList(gatewayLoadBalanceService, serviceId);
                    DynamicLoadBalancer dynamicLoadBalancer = new DynamicLoadBalancer(serverList);
                    dynamicLoadBalancer.init(loadbalanceInfo);
                    loadBalancerConcurrentHashMap.put(serviceId, dynamicLoadBalancer);
                    return dynamicLoadBalancer;
                }
                return loadBalancer;
            }
        };
    }
}
