package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.discovery.DiscoveryConnectionRepositoryManager;
import com.doctorwork.sword.gateway.discovery.DiscoveryRepositoryManager;
import com.doctorwork.sword.gateway.discovery.IDiscoveryConnectionRepository;
import com.doctorwork.sword.gateway.discovery.IDiscoveryRepository;
import com.doctorwork.sword.gateway.discovery.common.DiscoveryProperties;
import com.doctorwork.sword.gateway.discovery.common.builder.ZookeeperProperties;
import com.doctorwork.sword.gateway.discovery.config.DiscoveryConfig;
import com.doctorwork.sword.gateway.discovery.config.DiscoveryRegistryConfig;
import com.doctorwork.sword.gateway.loadbalance.CustomerLoadBalanceClient;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryConnectionService;
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
    public IDiscoveryConnectionRepository discoveryConnectionRepository(GatewayDiscoveryConnectionService gatewayDiscoveryConnectionService,
                                                                        ZookeeperProperties defaultZookeeperProperties) {
        DiscoveryRegistryConfig<ZookeeperProperties> discoveryRegistryConfig = null;
        if (defaultZookeeperProperties != null) {
            discoveryRegistryConfig = new DiscoveryRegistryConfig<>();
            discoveryRegistryConfig.setProperties(defaultZookeeperProperties);
            discoveryRegistryConfig.setRegistryKey(DiscoveryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
        }
        return new DiscoveryConnectionRepositoryManager(gatewayDiscoveryConnectionService, discoveryRegistryConfig);
    }

    @Bean
    public IDiscoveryRepository discoveryRepository(GatewayDiscoveryService gatewayDiscoveryService,
                                                    DiscoveryProperties defaultDiscoveryProperties,
                                                    IDiscoveryConnectionRepository discoveryConnectionRepository) throws Exception {
        DiscoveryConfig<DiscoveryProperties> discoveryConfig = null;

        if (defaultDiscoveryProperties != null) {
            discoveryConfig = new DiscoveryConfig<>(DiscoveryRepositoryManager.DEFAULT_SERVICEDISCOVERY,
                    true, DiscoveryConnectionRepositoryManager.DEFAULT_ZOOKEEPER, defaultDiscoveryProperties);
        }
        DiscoveryRepositoryManager discoveryRepositoryManager = new DiscoveryRepositoryManager(gatewayDiscoveryService, discoveryConfig, discoveryConnectionRepository);
        discoveryRepositoryManager.preLoadDiscovery();
        return discoveryRepositoryManager;
    }

    @Bean
    @ConditionalOnMissingBean
    public LoadBalancerClient loadBalancerClient(GatewayLoadBalanceService gatewayLoadBalanceService, IDiscoveryRepository discoveryRepository) {

        return new CustomerLoadBalanceClient(gatewayLoadBalanceService, discoveryRepository);
    }
}
