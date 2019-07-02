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

import java.util.List;
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
    private DiscoveryConfigParam defaultDiscoveryConfigParam;

    public CustomerLoadBalanceClient(GatewayLoadBalanceService gatewayLoadBalanceService, GatewayDiscoveryService gatewayDiscoveryService, DiscoveryConfigParam defaultDiscoveryConfigParam) {
        this.gatewayLoadBalanceService = gatewayLoadBalanceService;
        this.gatewayDiscoveryService = gatewayDiscoveryService;
        this.defaultDiscoveryConfigParam = defaultDiscoveryConfigParam;
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

    public void preLoadDiscovery() {
        logger.info("预加载服务发现配置");
        List<DiscoverConfig> preLoadList = gatewayDiscoveryService.preLoadList();
        //db preload
        for (DiscoverConfig discoverConfig : preLoadList) {
            String mark = ".".concat(discoverConfig.getDscrId());
            ServiceDiscovery<ZookeeperInstance> serviceDiscovery = discoveryMap.get(mark);
            if (serviceDiscovery == null) {
                DiscoveryConfigParam discoveryConfigParam = DiscoveryConfigParam.build(discoverConfig);
                ZookeeperProperties zookeeperProperties = discoveryConfigParam.getZookeeperProperties();
                DiscoveryProperties discoveryProperties = discoveryConfigParam.getDiscoveryProperties();
                serviceDiscovery = buildServiceDiscovery(zookeeperProperties, discoveryProperties);
                if (serviceDiscovery != null) {
                    discoveryMap.putIfAbsent(mark, serviceDiscovery);
                }
            }
        }
        //pool load
//        Map<String, String> poolMap = gatewayDiscoveryService.poolMap(preLoadList);
//        for (Map.Entry<String, String> entry : poolMap.entrySet()) {
//            ServiceDiscovery<ZookeeperInstance> serviceDiscovery = discoveryMap.get(entry.getValue());
//            if (serviceDiscovery != null)
//                discoveryMap.putIfAbsent(entry.getKey(), serviceDiscovery);
//        }
        //local preload
        if (defaultDiscoveryConfigParam != null && defaultDiscoveryConfigParam.isPreLoad() != null
                && defaultDiscoveryConfigParam.isPreLoad()) {
            String mark = DEFAULT_SERVICEDISCOVERY;
            ServiceDiscovery<ZookeeperInstance> serviceDiscovery = discoveryMap.get(mark);
            if (serviceDiscovery == null) {
                ZookeeperProperties defaultZookeeperProperties = defaultDiscoveryConfigParam.getZookeeperProperties();
                DiscoveryProperties defaultDiscoveryProperties = defaultDiscoveryConfigParam.getDiscoveryProperties();
                serviceDiscovery = buildServiceDiscovery(defaultZookeeperProperties, defaultDiscoveryProperties);
                discoveryMap.putIfAbsent(mark, serviceDiscovery);
            }
        }
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
                if (defaultDiscoveryConfigParam == null)
                    return null;
                //local properties
                mark = DEFAULT_SERVICEDISCOVERY;
                serviceDiscovery = discoveryMap.get(mark);
                if (serviceDiscovery != null) {
                    ServiceDiscovery<ZookeeperInstance> old = discoveryMap.putIfAbsent(serviceId, serviceDiscovery);
                    if (old == null) {
                        buildServiceCache(serviceId, serviceDiscovery);
                    }
                    return serviceDiscovery;
                }
                ZookeeperProperties defaultZookeeperProperties = defaultDiscoveryConfigParam.getZookeeperProperties();
                DiscoveryProperties defaultDiscoveryProperties = defaultDiscoveryConfigParam.getDiscoveryProperties();
                serviceDiscovery = buildServiceDiscovery(defaultZookeeperProperties, defaultDiscoveryProperties);
            } else {
                mark = "." + discoverConfig.getDscrId();
                serviceDiscovery = discoveryMap.get(mark);
                if (serviceDiscovery != null) {
                    ServiceDiscovery<ZookeeperInstance> old = discoveryMap.putIfAbsent(serviceId, serviceDiscovery);
                    if (old == null) {
                        buildServiceCache(serviceId, serviceDiscovery);
                    }
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
            ServiceDiscovery<ZookeeperInstance> old = discoveryMap.putIfAbsent(serviceId, serviceDiscovery);
            if (old == null) {
                buildServiceCache(serviceId, serviceDiscovery);
            }
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
            //cache listen
        } catch (Exception e) {
            logger.error("error create serviceDiscovery for default({})", discoveryProperties.getZkRoot(), e);
            return null;
        }
        return serviceDiscovery;
    }

    private void buildServiceCache(String serviceId, ServiceDiscovery<ZookeeperInstance> serviceDiscovery) {
        try {
            ServiceCache<ZookeeperInstance> serviceCache = serviceDiscovery.serviceCacheBuilder().name(serviceId).build();
            serviceCache.start();
            serviceCache.addListener(new ServiceCacheListener() {
                @Override
                public void cacheChanged() {
                    logger.warn("节点配置变更");
                }

                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) {
                    logger.warn("节点连接状态变更");
                }
            });
        } catch (Exception e) {
            logger.error("error create servicecache for default({})", serviceId, e);
        }

    }
}
