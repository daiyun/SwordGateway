package com.doctorwork.sword.gateway.config;

import com.doctorwork.com.sword.gateway.registry.RegistryConnectionRepositoryManager;
import com.doctorwork.com.sword.gateway.registry.IRegistryConnectionRepository;
import com.doctorwork.sword.gateway.discovery.DiscoveryRepositoryManager;
import com.doctorwork.sword.gateway.discovery.IDiscoveryRepository;
import com.doctorwork.sword.gateway.discovery.api.IRespositoryManagerApi;
import com.doctorwork.sword.gateway.discovery.api.RespositoryManagerApi;
import com.doctorwork.sword.gateway.discovery.common.DiscoveryProperties;
import com.doctorwork.sword.gateway.discovery.common.builder.ZookeeperProperties;
import com.doctorwork.sword.gateway.discovery.config.DiscoveryConfig;
import com.doctorwork.com.sword.gateway.registry.config.DiscoveryRegistryConfig;
import com.doctorwork.sword.gateway.loadbalance.CustomerLoadBalanceClient;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryConnectionService;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryService;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
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
    public IRegistryConnectionRepository discoveryConnectionRepository(GatewayDiscoveryConnectionService gatewayDiscoveryConnectionService,
                                                                       ZookeeperProperties defaultZookeeperProperties,
                                                                       EventBus eventBus) {
        DiscoveryRegistryConfig<ZookeeperProperties> discoveryRegistryConfig = null;
        if (defaultZookeeperProperties != null) {
            discoveryRegistryConfig = new DiscoveryRegistryConfig<>();
            discoveryRegistryConfig.setProperties(defaultZookeeperProperties);
            discoveryRegistryConfig.setRegistryKey(RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
        }
        return new RegistryConnectionRepositoryManager(gatewayDiscoveryConnectionService, discoveryRegistryConfig, eventBus);
    }

    @Bean
    public IDiscoveryRepository discoveryRepository(GatewayDiscoveryService gatewayDiscoveryService,
                                                    DiscoveryProperties defaultDiscoveryProperties,
                                                    IRegistryConnectionRepository discoveryConnectionRepository,
                                                    @Autowired(required = false) EventBus eventBus) throws Exception {
        DiscoveryConfig<DiscoveryProperties> discoveryConfig = null;

        if (defaultDiscoveryProperties != null) {
            discoveryConfig = new DiscoveryConfig<>(DiscoveryRepositoryManager.DEFAULT_SERVICEDISCOVERY,
                    true, RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER, defaultDiscoveryProperties);
        }
        DiscoveryRepositoryManager discoveryRepositoryManager = new DiscoveryRepositoryManager(gatewayDiscoveryService, discoveryConfig,
                discoveryConnectionRepository, eventBus);
        discoveryRepositoryManager.preLoadDiscovery();
        return discoveryRepositoryManager;
    }

    @Bean
    public IRespositoryManagerApi respositoryManagerApi(IRegistryConnectionRepository discoveryConnectionRepository, IDiscoveryRepository discoveryRepository) {
        return new RespositoryManagerApi(discoveryConnectionRepository, discoveryRepository);
    }

    @Bean
    public LoadBalancerClient loadBalancerClient(GatewayLoadBalanceService gatewayLoadBalanceService,
                                                 IDiscoveryRepository discoveryRepository,
                                                 EventBus eventBus) {
        CustomerLoadBalanceClient customerLoadBalanceClient = new CustomerLoadBalanceClient(gatewayLoadBalanceService, discoveryRepository, eventBus);
        return customerLoadBalanceClient;
    }
}
