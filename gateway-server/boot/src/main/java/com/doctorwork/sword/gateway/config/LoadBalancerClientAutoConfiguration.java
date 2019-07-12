package com.doctorwork.sword.gateway.config;

import com.doctorwork.com.sword.gateway.registry.IRegistryConnectionRepository;
import com.doctorwork.com.sword.gateway.registry.RegistryConnectionRepositoryManager;
import com.doctorwork.com.sword.gateway.registry.config.RegistryConfig;
import com.doctorwork.sword.gateway.common.config.IDiscoveryConfigRepository;
import com.doctorwork.sword.gateway.common.config.ILoadBalancerConfigRepository;
import com.doctorwork.sword.gateway.discovery.DiscoveryRepositoryManager;
import com.doctorwork.sword.gateway.discovery.IDiscoveryRepository;
import com.doctorwork.sword.gateway.discovery.api.IRespositoryManagerApi;
import com.doctorwork.sword.gateway.discovery.api.RespositoryManagerApi;
import com.doctorwork.sword.gateway.discovery.common.DiscoveryProperties;
import com.doctorwork.sword.gateway.discovery.common.builder.ZookeeperProperties;
import com.doctorwork.sword.gateway.discovery.config.DiscoveryConfig;
import com.doctorwork.sword.gateway.loadbalance.CustomerLoadBalanceClient;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryConnectionService;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryService;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;
import com.google.common.eventbus.EventBus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
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
    public LoadBalancerProperties loadBalancerProperties() {
        LoadBalancerProperties properties = new LoadBalancerProperties();
        properties.setUse404(true);
        return properties;
    }

    @Bean
    @ConditionalOnMissingBean({LoadBalancerClientFilter.class})
    public LoadBalancerClientFilter loadBalancerClientFilter(LoadBalancerClient client, LoadBalancerProperties loadBalancerProperties) {

        return new LoadBalancerClientFilter(client, loadBalancerProperties);
    }

    @Bean
    public ConfigManager configManager(GatewayConfig gatewayConfig, GatewayDiscoveryService gatewayDiscoveryService,
                                       GatewayDiscoveryConnectionService gatewayDiscoveryConnectionService,
                                       GatewayLoadBalanceService gatewayLoadBalanceService, IRegistryConnectionRepository registryConnectionRepository,
                                       EventBus eventBus) {
        DataBaseConfigRepository dataBaseConfigRepository = new DataBaseConfigRepository(gatewayLoadBalanceService, gatewayDiscoveryService, gatewayDiscoveryConnectionService, gatewayConfig);
        RegistryConfigRepository registryConfigRepository = new RegistryConfigRepository(registryConnectionRepository, eventBus, gatewayConfig);
        ConfigManager configManager = new ConfigManager(gatewayConfig, dataBaseConfigRepository, registryConfigRepository);
        registryConnectionRepository.setConnectionConfig(configManager);
        return configManager;
    }

    @Bean
    public IRegistryConnectionRepository discoveryConnectionRepository(ZookeeperProperties defaultZookeeperProperties,
                                                                       EventBus eventBus) {
        RegistryConfig<ZookeeperProperties> registryConfig = null;
        if (defaultZookeeperProperties != null) {
            registryConfig = new RegistryConfig<>();
            registryConfig.setProperties(defaultZookeeperProperties);
            registryConfig.setRegistryKey(RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
        }
        return new RegistryConnectionRepositoryManager(registryConfig, eventBus);
    }

    @Bean
    public IDiscoveryRepository discoveryRepository(IDiscoveryConfigRepository discoveryConfigRepository,
                                                    DiscoveryProperties defaultDiscoveryProperties,
                                                    IRegistryConnectionRepository discoveryConnectionRepository,
                                                    EventBus eventBus) throws Exception {
        DiscoveryConfig<DiscoveryProperties> discoveryConfig = null;

        if (defaultDiscoveryProperties != null) {
            discoveryConfig = new DiscoveryConfig<>(DiscoveryRepositoryManager.DEFAULT_SERVICEDISCOVERY,
                    true, RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER, defaultDiscoveryProperties);
        }
        DiscoveryRepositoryManager discoveryRepositoryManager = new DiscoveryRepositoryManager(discoveryConfigRepository, discoveryConfig,
                discoveryConnectionRepository, eventBus);
        return discoveryRepositoryManager;
    }

    @Bean
    public IRespositoryManagerApi respositoryManagerApi(IRegistryConnectionRepository discoveryConnectionRepository, IDiscoveryRepository discoveryRepository) {
        return new RespositoryManagerApi(discoveryConnectionRepository, discoveryRepository);
    }

    @Bean
    public CustomerLoadBalanceClient loadBalancerClient(ILoadBalancerConfigRepository loadBalancerConfigRepository,
                                                        IDiscoveryRepository discoveryRepository,
                                                        EventBus eventBus) {
        return new CustomerLoadBalanceClient(loadBalancerConfigRepository, discoveryRepository, eventBus);
    }
}
