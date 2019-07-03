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
import org.apache.curator.x.discovery.ServiceDiscovery;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:27 2019/7/2
 * @Modified By:
 */
public class CustomerLoadBalanceClient extends AbstractLoadBalanceClient implements IDiscoveryRepository {

    private final Map<String, ILoadBalancer> loadBalancerMap = new ConcurrentHashMap<>();
    private final Map<String, ServiceDiscovery<ZookeeperInstance>> discoveryMap = new ConcurrentHashMap<>();
    private final Map<String, ServiceDiscoveryWrapper> discoveryWrapperMap = new ConcurrentHashMap<>();
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
                    ServiceDiscoveryWrapper wrapper = this.serviceWrapper(serviceId);
                    serverList = new CompositiveServerList(serviceId, new DataBaseServerList(serviceId, gatewayLoadBalanceService),
                            new ZookeeperServerList(serviceId, wrapper));
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
        List<DiscoverConfig> preLoadList = gatewayDiscoveryService.preLoadList();
        //pre load
        Map<String, DiscoverConfig> poolMap = gatewayDiscoveryService.poolMap(preLoadList);
        for (Map.Entry<String, DiscoverConfig> entry : poolMap.entrySet()) {
            logger.info("pre load service discovery config for {}", entry.getKey());
            loadService(entry.getKey(), entry.getValue());
        }
        //local preload
        synchronized (discoveryMap) {
            if (defaultDiscoveryConfigParam != null && defaultDiscoveryConfigParam.isPreLoad() != null
                    && defaultDiscoveryConfigParam.isPreLoad()) {
                logger.info("pre load service discovery config for local");
                String mark = DEFAULT_SERVICEDISCOVERY;
                ServiceDiscovery<ZookeeperInstance> serviceDiscovery = discoveryMap.get(mark);
                if (serviceDiscovery == null) {
                    ZookeeperProperties defaultZookeeperProperties = defaultDiscoveryConfigParam.getZookeeperProperties();
                    DiscoveryProperties defaultDiscoveryProperties = defaultDiscoveryConfigParam.getDiscoveryProperties();
                    serviceDiscovery = buildServiceDiscovery(defaultZookeeperProperties, defaultDiscoveryProperties);
                    if (serviceDiscovery != null) {
                        discoveryMap.putIfAbsent(mark, serviceDiscovery);
                    }
                }
            }
        }
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

    @Override
    public ServiceDiscoveryWrapper serviceWrapper(String serviceId) {
        ServiceDiscoveryWrapper serviceDiscoveryWrapper = discoveryWrapperMap.get(serviceId);
        if (serviceDiscoveryWrapper != null)
            return serviceDiscoveryWrapper;
        synchronized (discoveryWrapperMap) {
            //double check
            serviceDiscoveryWrapper = discoveryWrapperMap.get(serviceId);
            if (serviceDiscoveryWrapper != null)
                return serviceDiscoveryWrapper;
            ServiceDiscoveryWrapper wrapper = new ServiceDiscoveryWrapper(serviceId, null, this);
            discoveryWrapperMap.put(serviceId, wrapper);
            return serviceDiscoveryWrapper;
        }
    }

    @Override
    public ServiceDiscovery<ZookeeperInstance> serviceDisovery(String serviceId) {
        ServiceDiscoveryWrapper wrapper = serviceWrapper(serviceId);
        if (wrapper == null)
            return null;
        return discoveryMap.get(wrapper.getDscrMapKey());
    }

    @Override
    public void reload() {
        //is necessary?
    }

    @Override
    public void loadService(String serviceId, DiscoverConfig discoverConfig) {
        DiscoverConfig config = discoverConfig;
        if (discoverConfig == null)
            config = gatewayDiscoveryService.serviceTodiscoverConfig(serviceId);
        ZookeeperProperties zookeeperProperties;
        DiscoveryProperties discoveryProperties;
        String mark;
        if (config == null) {
            if (defaultDiscoveryConfigParam == null)
                return;
            //local properties
            mark = DEFAULT_SERVICEDISCOVERY;
            zookeeperProperties = defaultDiscoveryConfigParam.getZookeeperProperties();
            discoveryProperties = defaultDiscoveryConfigParam.getDiscoveryProperties();
        } else {
            mark = "." + config.getDscrId();
            DiscoveryConfigParam discoveryConfigParam = DiscoveryConfigParam.build(config);
            zookeeperProperties = discoveryConfigParam.getZookeeperProperties();
            discoveryProperties = discoveryConfigParam.getDiscoveryProperties();
        }
        ServiceDiscovery<ZookeeperInstance> serviceDiscovery = discoveryMap.get(mark);
        if (serviceDiscovery == null) {
            synchronized (discoveryMap) {
                //double check
                serviceDiscovery = discoveryMap.get(mark);
                if (serviceDiscovery == null) {
                    serviceDiscovery = buildServiceDiscovery(zookeeperProperties, discoveryProperties);
                    if (serviceDiscovery == null) {
                        logger.warn("service discovery for {} can not be created", mark);
                        return;
                    }
                    discoveryMap.put(mark, serviceDiscovery);
                }
            }
        }
        synchronized (discoveryWrapperMap) {
            ServiceDiscoveryWrapper serviceDiscoveryWrapper = discoveryWrapperMap.get(serviceId);
            if (serviceDiscoveryWrapper == null) {
                ServiceDiscoveryWrapper saveWrapper = new ServiceDiscoveryWrapper(serviceId, mark, this);
                discoveryWrapperMap.put(serviceId, saveWrapper);
            } else {
                serviceDiscoveryWrapper.reload(mark);
            }
        }
    }

    @Override
    public void loadDiscovery(String dscrId, DiscoverConfig discoverConfig) throws IOException {
        DiscoverConfig config = discoverConfig;
        if (discoverConfig == null)
            config = gatewayDiscoveryService.discoverConfig(dscrId);
        ZookeeperProperties zookeeperProperties;
        DiscoveryProperties discoveryProperties;
        String mark;
        if (config == null) {
            if (defaultDiscoveryConfigParam == null)
                return;
            //local properties
            mark = DEFAULT_SERVICEDISCOVERY;
            zookeeperProperties = defaultDiscoveryConfigParam.getZookeeperProperties();
            discoveryProperties = defaultDiscoveryConfigParam.getDiscoveryProperties();
        } else {
            mark = "." + config.getDscrId();
            DiscoveryConfigParam discoveryConfigParam = DiscoveryConfigParam.build(config);
            zookeeperProperties = discoveryConfigParam.getZookeeperProperties();
            discoveryProperties = discoveryConfigParam.getDiscoveryProperties();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("LOCK----").append(dscrId);
        synchronized (sb.toString().intern()) {
            ServiceDiscovery<ZookeeperInstance> serviceDiscovery = buildServiceDiscovery(zookeeperProperties, discoveryProperties);
            if (serviceDiscovery == null) {
                logger.warn("service discovery for {} can not be created", dscrId);
                return;
            }
            ServiceDiscovery<ZookeeperInstance> old = discoveryMap.get(mark);
            discoveryMap.put(mark, serviceDiscovery);
            for (ServiceDiscoveryWrapper wrapper : discoveryWrapperMap.values()) {
                if(mark.equals(wrapper.getDscrMapKey())){
                    wrapper.reloadCache();
                }
            }
            if (old != null) {
                old.close();
            }
        }
    }

    @Override
    public void delete(String serviceId) {
        ServiceDiscoveryWrapper serviceDiscoveryWrapper = discoveryWrapperMap.get(serviceId);
        if (serviceDiscoveryWrapper == null)
            return;
        synchronized (discoveryWrapperMap) {
            //double check
            ServiceDiscoveryWrapper wrapper = discoveryWrapperMap.get(serviceId);
            if (wrapper == null)
                return;
            wrapper.clear();
            discoveryWrapperMap.remove(serviceId);
        }
    }
}
