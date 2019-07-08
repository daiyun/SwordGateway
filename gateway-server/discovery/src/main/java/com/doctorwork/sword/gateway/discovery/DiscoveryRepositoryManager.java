package com.doctorwork.sword.gateway.discovery;

import com.doctorwork.sword.gateway.common.event.AbstractEvent;
import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.doctorwork.sword.gateway.discovery.config.DiscoveryConfig;
import com.doctorwork.sword.gateway.discovery.connection.IQueryService;
import com.doctorwork.sword.gateway.discovery.connection.ServiceDiscoveryWrapper;
import com.doctorwork.sword.gateway.discovery.event.EventPost;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryService;
import com.google.common.eventbus.EventBus;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:10 2019/7/4
 * @Modified By:
 */
public class DiscoveryRepositoryManager implements IDiscoveryRepository, EventPost {
    protected static final Logger logger = LoggerFactory.getLogger(DiscoveryRepositoryManager.class);
    private GatewayDiscoveryService gatewayDiscoveryService;
    private DiscoveryConfig defaultDiscoveryConfig;
    private IDiscoveryConnectionRepository discoveryConnectionRepository;
    private final Map<String, ServiceDiscoveryWrapper> discoveryMap = new ConcurrentHashMap<>();
    private final Map<String, ServiceWrapper> serviceWrapperMap = new ConcurrentHashMap<>();
    public static final String DEFAULT_SERVICEDISCOVERY = "default";
    private EventBus eventBus;

    public DiscoveryRepositoryManager(GatewayDiscoveryService gatewayDiscoveryService, DiscoveryConfig defaultDiscoveryConfig, IDiscoveryConnectionRepository discoveryConnectionRepository, EventBus eventBus) {
        this.gatewayDiscoveryService = gatewayDiscoveryService;
        this.defaultDiscoveryConfig = defaultDiscoveryConfig;
        this.discoveryConnectionRepository = discoveryConnectionRepository;
        this.eventBus = eventBus;
    }

    public void preLoadDiscovery() throws Exception {
        //local preload
        synchronized (discoveryMap) {
            if (defaultDiscoveryConfig != null && defaultDiscoveryConfig.isPreLoad() != null
                    && defaultDiscoveryConfig.isPreLoad()) {
                logger.info("pre connectionLoad service discovery config for local");
                discoveryConnectionRepository.connectionLoad("default", this);
                this.loadDiscovery("default", null);
            }
        }
        List<DiscoverConfig> preLoadList = gatewayDiscoveryService.preLoadList();
        Set<String> regisryIdSet = preLoadList.stream().map(DiscoverConfig::getDscrRegitryId).collect(Collectors.toSet());
        for (String registryId : regisryIdSet) {
            discoveryConnectionRepository.connectionLoad(registryId, this);
        }
        for (DiscoverConfig discoverConfig : preLoadList) {
            this.loadDiscovery(discoverConfig.getDscrId(), discoverConfig);
        }
        //pre connectionLoad
        Map<String, DiscoverConfig> poolMap = gatewayDiscoveryService.poolMap(preLoadList);
        for (Map.Entry<String, DiscoverConfig> entry : poolMap.entrySet()) {
            logger.info("pre connectionLoad service discovery config for {}", entry.getKey());
            loadService(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public ServiceWrapper serviceWrapper(String serviceId) {
        ServiceWrapper serviceWrapper = serviceWrapperMap.get(serviceId);
        if (serviceWrapper != null)
            return serviceWrapper;
        synchronized (serviceWrapperMap) {
            //double check
            serviceWrapper = serviceWrapperMap.get(serviceId);
            if (serviceWrapper != null)
                return serviceWrapper;
            //此处会有高并发情况
            DiscoverConfig discoverConfig = gatewayDiscoveryService.discoverConfig(serviceId);
            if (discoverConfig == null) {
                logger.error("no discover config for {}", serviceId);
                return null;
            }
            ServiceWrapper wrapper = new ServiceWrapper(serviceId, discoverConfig.getDscrId(), this, this);
            serviceWrapperMap.put(serviceId, wrapper);
            return wrapper;
        }
    }

    @Override
    public ServiceDiscoveryWrapper serviceDisovery(ServiceWrapper serviceWrapper) {
        return discoveryMap.get(serviceWrapper.getDscrMapKey());
    }

    @Override
    public void loadService(String serviceId, DiscoverConfig discoverConfig) throws Exception {
        DiscoverConfig config = discoverConfig;
        if (discoverConfig == null)
            config = gatewayDiscoveryService.serviceTodiscoverConfig(serviceId);
        String mark;
        if (config == null) {
            return;
        }
        mark = config.getDscrId();
//                synchronized (serviceWrapperMap) {
        ServiceWrapper serviceWrapper = serviceWrapperMap.get(serviceId);
        if (serviceWrapper == null) {
            ServiceWrapper saveWrapper = new ServiceWrapper(serviceId, mark, this, this);
            serviceWrapperMap.put(serviceId, saveWrapper);
        } else {
            serviceWrapper.reload(mark);
        }
//                }
    }

    @Override
    public void loadDiscovery(String dscrId, DiscoverConfig discoverConfig) throws Exception {
        DiscoverConfig config = discoverConfig;
        if (discoverConfig == null)
            config = gatewayDiscoveryService.discoverConfig(dscrId);
        String mark;
        DiscoveryConfig discoveryConfig = null;
        if (DEFAULT_SERVICEDISCOVERY.equals(dscrId)) {
            if (config == null && defaultDiscoveryConfig != null) {
                discoveryConfig = defaultDiscoveryConfig;
            }
        }
        if (config == null && discoveryConfig == null) {
            return;
        }
        mark = dscrId;
        if (discoveryConfig == null)
            discoveryConfig = DiscoveryConfig.build(config);
        synchronized (("LOCK----" + dscrId).intern()) {
            ServiceDiscoveryWrapper serviceDiscoveryWrapper = discoveryConfig.buildServiceDiscovery(discoveryConnectionRepository);
            if (serviceDiscoveryWrapper == null) {
                logger.warn("service discovery for {} can not be created", dscrId);
                return;
            }
            ServiceDiscoveryWrapper old = discoveryMap.get(mark);
            discoveryMap.put(mark, serviceDiscoveryWrapper);
            for (ServiceWrapper wrapper : serviceWrapperMap.values()) {
                if (mark.equals(wrapper.getDscrMapKey())) {
                    wrapper.reloadCache();
                }
            }
            if (old != null) {
                old.close();
            }
        }

    }

    //注册中心重载需要触发相应的服务更改
    @Override
    public void loadRegistry(String registryId) throws Exception {
        if (StringUtils.isEmpty(registryId))
            return;
        for (ServiceDiscoveryWrapper discoveryWrapper : discoveryMap.values()) {
            if (registryId.equals(discoveryWrapper.getConnectionId())) {
                this.loadDiscovery(discoveryWrapper.getId(), null);
            }
        }
    }

    @Override
    public void serviceDelete(String serviceId) {
        ServiceWrapper serviceWrapper = serviceWrapperMap.get(serviceId);
        if (serviceWrapper == null)
            return;
        synchronized (serviceWrapperMap) {
            //double check
            ServiceWrapper wrapper = serviceWrapperMap.get(serviceId);
            if (wrapper == null)
                return;
            wrapper.clear();
            serviceWrapperMap.remove(serviceId);
        }
    }

    @Override
    public Collection<ServiceInstance<ZookeeperInstance>> queryServices(String serviceId) throws Exception {
        ServiceWrapper serviceWrapper = this.serviceWrapper(serviceId);
        if (serviceWrapper == null) {
            logger.error("could not find service wrapper for {}", serviceId);
            return null;
        }
        IQueryService queryService = serviceWrapper.queryService();
        if (queryService == null) {
            logger.error("could not find query service for {}", serviceId);
            return null;
        }
        return queryService.getInstances(serviceId);
    }

    @Override
    public void eventPost(AbstractEvent event) {
        if (eventBus != null)
            eventBus.post(event);
    }
}
