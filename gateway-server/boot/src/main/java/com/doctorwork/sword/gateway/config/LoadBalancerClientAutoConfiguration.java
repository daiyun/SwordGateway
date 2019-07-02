package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.discovery.common.DiscoveryProperties;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import com.doctorwork.sword.gateway.discovery.common.builder.CuratorBuilder;
import com.doctorwork.sword.gateway.discovery.common.builder.DiscoveryBuilder;
import com.doctorwork.sword.gateway.discovery.common.builder.ZookeeperProperties;
import com.doctorwork.sword.gateway.loadbalance.AbstractLoadBalanceClient;
import com.doctorwork.sword.gateway.loadbalance.DynamicLoadBalancer;
import com.doctorwork.sword.gateway.loadbalance.param.DiscoveryConfigParam;
import com.doctorwork.sword.gateway.loadbalance.server.CompositiveServerList;
import com.doctorwork.sword.gateway.loadbalance.server.DataBaseServerList;
import com.doctorwork.sword.gateway.loadbalance.server.ZookeeperServerList;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryService;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
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

    private static final Map<String, ILoadBalancer> loadBalancerMap = new ConcurrentHashMap<>();
    private static final Map<String, ServiceDiscovery<ZookeeperInstance>> discoveryMap = new ConcurrentHashMap<>();
    private static final String DEFAULT_SERVICEDISCOVERY = ".default";

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
                            return null;
                        loadBalancer = loadBalancerMap.get(serviceId);
                        if (loadBalancer != null) {
                            return loadBalancer;
                        }
                        ServerList serverList;
                        if (loadbalanceInfo.getDscrEnable() == null || loadbalanceInfo.getDscrEnable().equals(0)) {
                            serverList = new DataBaseServerList(serviceId, gatewayLoadBalanceService);
                        } else {
                            ServiceDiscovery<ZookeeperInstance> serviceDiscovery = serviceDiscovery(serviceId);
                            if (serviceDiscovery == null) {
                                serverList = new DataBaseServerList(serviceId, gatewayLoadBalanceService);
                            } else {
                                serverList = new CompositiveServerList(serviceId, new DataBaseServerList(serviceId, gatewayLoadBalanceService),
                                        new ZookeeperServerList(serviceId, serviceDiscovery));
                            }
                        }
                        DynamicLoadBalancer dynamicLoadBalancer = new DynamicLoadBalancer(serverList);
                        dynamicLoadBalancer.init(loadbalanceInfo);
                        loadBalancerMap.putIfAbsent(serviceId, dynamicLoadBalancer);
                    }
                    return loadBalancerMap.get(serviceId);
                }
                return loadBalancer;
            }

            private ServiceDiscovery<ZookeeperInstance> serviceDiscovery(String serviceId) {
                ServiceDiscovery<ZookeeperInstance> serviceDiscovery = discoveryMap.get(serviceId);
                if (serviceDiscovery != null) {
                    return serviceDiscovery;
                }
                synchronized (discoveryMap) {
                    serviceDiscovery = discoveryMap.get(serviceId);
                    if (serviceDiscovery != null)
                        return serviceDiscovery;
                    DiscoverConfig discoverConfig = gatewayDiscoveryService.discoverConfig(serviceId);
                    String mark;
                    if (discoverConfig == null) {
                        //local properties
                        mark = DEFAULT_SERVICEDISCOVERY;
                        serviceDiscovery = discoveryMap.get(mark);
                        if (serviceDiscovery != null) {
                            discoveryMap.putIfAbsent(serviceId, serviceDiscovery);
                            return serviceDiscovery;
                        }
                        serviceDiscovery = buildServiceDiscovery(defaultZookeeperProperties, defaultDiscoveryProperties);
                    } else {
                        mark = "." + discoverConfig.getDscrId();
                        serviceDiscovery = discoveryMap.get(mark);
                        if (serviceDiscovery != null) {
                            discoveryMap.putIfAbsent(serviceId, serviceDiscovery);
                            return serviceDiscovery;
                        }
                        DiscoveryConfigParam discoveryConfigParam = DiscoveryConfigParam.build(discoverConfig);
                        ZookeeperProperties zookeeperProperties = discoveryConfigParam.getZookeeperProperties();
                        DiscoveryProperties discoveryProperties = discoveryConfigParam.getDiscoveryProperties();
                        serviceDiscovery = buildServiceDiscovery(zookeeperProperties, discoveryProperties);
                    }
                    if (serviceDiscovery == null)
                        return null;
                    discoveryMap.putIfAbsent(mark, serviceDiscovery);
                    discoveryMap.putIfAbsent(serviceId, serviceDiscovery);
                }
                return discoveryMap.get(serviceId);
            }

            private ServiceDiscovery<ZookeeperInstance> buildServiceDiscovery(ZookeeperProperties zookeeperProperties, DiscoveryProperties discoveryProperties) {
                CuratorBuilder defaultCuratorBuilder = new CuratorBuilder(zookeeperProperties);
                CuratorFramework defaultCuratorFramework = defaultCuratorBuilder.build();
                DiscoveryBuilder defaultDiscoveryBuilder = new DiscoveryBuilder(defaultCuratorFramework, discoveryProperties.getZkRoot());
                ServiceDiscovery<ZookeeperInstance> serviceDiscovery = defaultDiscoveryBuilder.build();
                try {
                    serviceDiscovery.start();
                } catch (Exception e) {
                    logger.error("error create serviceDiscovery for default({})", discoveryProperties.getZkRoot(), e);
                    return null;
                }
                return serviceDiscovery;
            }
        };
    }
}
