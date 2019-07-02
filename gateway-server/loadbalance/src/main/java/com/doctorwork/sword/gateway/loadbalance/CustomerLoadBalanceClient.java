package com.doctorwork.sword.gateway.loadbalance;

import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.discovery.common.DiscoveryProperties;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import com.doctorwork.sword.gateway.discovery.common.builder.CuratorBuilder;
import com.doctorwork.sword.gateway.discovery.common.builder.DiscoveryBuilder;
import com.doctorwork.sword.gateway.discovery.common.builder.ZookeeperProperties;
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
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.details.ServiceCacheListener;

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
    private final Map<String, ServiceDiscovery<ZookeeperInstance>> discoveryMap = new ConcurrentHashMap<>();
    private static final String DEFAULT_SERVICEDISCOVERY = ".default";

    private GatewayLoadBalanceService gatewayLoadBalanceService;
    private GatewayDiscoveryService gatewayDiscoveryService;
    private ZookeeperProperties defaultZookeeperProperties;
    private DiscoveryProperties defaultDiscoveryProperties;

    public CustomerLoadBalanceClient(GatewayLoadBalanceService gatewayLoadBalanceService, GatewayDiscoveryService gatewayDiscoveryService, ZookeeperProperties defaultZookeeperProperties, DiscoveryProperties defaultDiscoveryProperties) {
        this.gatewayLoadBalanceService = gatewayLoadBalanceService;
        this.gatewayDiscoveryService = gatewayDiscoveryService;
        this.defaultZookeeperProperties = defaultZookeeperProperties;
        this.defaultDiscoveryProperties = defaultDiscoveryProperties;
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
        //simple
        ServiceCache<ZookeeperInstance> serviceCache = serviceDiscovery.serviceCacheBuilder().build();
        serviceCache.addListener(new ServiceCacheListener() {
            @Override
            public void cacheChanged() {

            }

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {

            }
        });
        return serviceDiscovery;
    }
}
