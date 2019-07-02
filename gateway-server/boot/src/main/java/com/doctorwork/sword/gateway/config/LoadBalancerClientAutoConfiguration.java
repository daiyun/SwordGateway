package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.discovery.common.DiscoveryProperties;
import com.doctorwork.sword.gateway.discovery.common.builder.ZookeeperProperties;
import com.doctorwork.sword.gateway.loadbalance.CustomerLoadBalanceClient;
import com.doctorwork.sword.gateway.loadbalance.param.DiscoveryConfigParam;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryService;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:03 2019/6/18
 * @Modified By:
 */
@Configuration
public class LoadBalancerClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean({LoadBalancerClientFilter.class})
    public LoadBalancerClientFilter loadBalancerClientFilter(LoadBalancerClient client) {
        return new LoadBalancerClientFilter(client);
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadBalancerClient loadBalancerClient(GatewayLoadBalanceService gatewayLoadBalanceService,
                                                 GatewayDiscoveryService gatewayDiscoveryService,
                                                 DiscoveryProperties defaultDiscoveryProperties,
                                                 ZookeeperProperties defaultZookeeperProperties) {
        DiscoveryConfigParam discoveryConfigParam = null;
        if (defaultDiscoveryProperties != null && defaultZookeeperProperties != null) {
            discoveryConfigParam = new DiscoveryConfigParam();
            discoveryConfigParam.setZookeeperProperties(defaultZookeeperProperties);
            discoveryConfigParam.setDiscoveryProperties(defaultDiscoveryProperties);
        }
        CustomerLoadBalanceClient customerLoadBalanceClient = new CustomerLoadBalanceClient(gatewayLoadBalanceService, gatewayDiscoveryService, discoveryConfigParam);
        customerLoadBalanceClient.preLoadDiscovery();
        return customerLoadBalanceClient;
    }
}
