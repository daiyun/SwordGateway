package com.doctorwork.sword.gateway.discovery;

import com.doctorwork.com.sword.gateway.registry.IRegistryConnectionRepository;
import com.doctorwork.sword.gateway.common.config.DiscoveryInfo;
import com.doctorwork.sword.gateway.common.config.IDiscoveryConfigRepository;
import com.doctorwork.sword.gateway.common.event.*;
import com.doctorwork.sword.gateway.common.listener.EventListener;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.doctorwork.sword.gateway.discovery.config.DiscoveryConfig;
import com.doctorwork.sword.gateway.discovery.connection.ServiceDiscoveryWrapper;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.curator.x.discovery.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:10 2019/7/4
 * @Modified By:
 */
public class DiscoveryRepositoryManager implements IDiscoveryRepository, EventPost, EventListener<AbstractEvent> {
    protected static final Logger logger = LoggerFactory.getLogger(DiscoveryRepositoryManager.class);
    private IDiscoveryConfigRepository discoveryConfigRepository;
    private DiscoveryConfig defaultDiscoveryConfig;
    private IRegistryConnectionRepository discoveryConnectionRepository;
    private final Map<String, ServiceDiscoveryWrapper> discoveryMap = new ConcurrentHashMap<>();
    private final Map<String, ServiceWrapper> serviceWrapperMap = new ConcurrentHashMap<>();
    public static final String DEFAULT_SERVICEDISCOVERY = "default";
    private EventBus eventBus;

    public DiscoveryRepositoryManager(IDiscoveryConfigRepository discoveryConfigRepository, DiscoveryConfig defaultDiscoveryConfig, IRegistryConnectionRepository discoveryConnectionRepository, EventBus eventBus) throws Exception {
        this.discoveryConfigRepository = discoveryConfigRepository;
        this.defaultDiscoveryConfig = defaultDiscoveryConfig;
        this.discoveryConnectionRepository = discoveryConnectionRepository;
        this.eventBus = eventBus;
        register(this.eventBus);
        preLoadDiscovery();
    }

    private void preLoadDiscovery() throws Exception {
        Collection<DiscoveryInfo> discoveryInfos = discoveryConfigRepository.all();
        for (DiscoveryInfo discoveryInfo : discoveryInfos) {
            this.loadDiscovery(discoveryInfo.getId(), discoveryInfo);
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
            DiscoveryInfo discoveryInfo = discoveryConfigRepository.discoveryConfigFromLoadBalance(serviceId);
            if (discoveryInfo == null) {
                logger.error("no discovery config for {}", serviceId);
                return null;
            }
            ServiceWrapper wrapper = new ServiceWrapper(serviceId, discoveryInfo.getId(), this, this);
            serviceWrapperMap.put(serviceId, wrapper);
            return wrapper;
        }
    }

    @Override
    public ServiceDiscoveryWrapper serviceDisovery(ServiceWrapper serviceWrapper) {
        return discoveryMap.get(serviceWrapper.getDscrMapKey());
    }

    @Override
    public void loadService(String serviceId, DiscoveryInfo discoveryInfo) throws Exception {
        DiscoveryInfo config = discoveryInfo;
        if (discoveryInfo == null)
            config = discoveryConfigRepository.discoveryConfigFromLoadBalance(serviceId);
        String mark;
        if (config == null) {
            return;
        }
        mark = config.getId();
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
    public void loadDiscovery(String dscrId, DiscoveryInfo discoveryInfo) throws Exception {
        DiscoveryInfo config = discoveryInfo;
        if (discoveryInfo == null)
            config = discoveryConfigRepository.discoveryConfig(dscrId);
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
        Map<String, String> serviceMap = new HashMap<>();
        boolean postFlag = false;
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
                    serviceMap.put(wrapper.getServiceId(), wrapper.getDscrMapKey());
                }
            }
            if (old != null) {
                postFlag = true;
                old.close();
            }
        }
        if (postFlag) {
            for (Map.Entry<String, String> serviceEntry : serviceMap.entrySet()) {
                eventPost(new ServiceCacheChangeEvent(serviceEntry.getKey(), serviceEntry.getValue()));
            }
        }
    }

    public synchronized void discoveryDelete(String dscrId) {
        Map<String, String> serviceMap = new HashMap<>();
        ServiceDiscoveryWrapper discoveryWrapper = discoveryMap.get(dscrId);
        if (discoveryWrapper == null)
            return;
        discoveryMap.remove(dscrId);
        for (ServiceWrapper wrapper : serviceWrapperMap.values()) {
            if (dscrId.equals(wrapper.getDscrMapKey())) {
                wrapper.reloadCache();
                serviceMap.put(wrapper.getServiceId(), wrapper.getDscrMapKey());
            }
        }
        discoveryWrapper.close();
        for (Map.Entry<String, String> serviceEntry : serviceMap.entrySet()) {
            eventPost(new ServiceCacheChangeEvent(serviceEntry.getKey(), serviceEntry.getValue()));
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

    @Override
    @Subscribe
    public void handleEvent(AbstractEvent event) {
        if (event instanceof RegistryLoadEvent) {
            RegistryLoadEvent registryLoadEvent = (RegistryLoadEvent) event;
            String registryId = registryLoadEvent.getRegistryId();
            logger.info("[RegistryLoadEvent]handle event load[{}] for {}", registryLoadEvent.getReload(), registryId);
            if (registryLoadEvent.getReload()) {
                try {
                    this.loadRegistry(registryId);
                } catch (Exception e) {
                    logger.error("[RegistryLoadEvent]handle event for {},but error happened", registryId, e);
                } finally {
                    registryLoadEvent.eventCall();
                }
            }
        } else if (event instanceof DiscoverConfigLoadEvent) {
            DiscoverConfigLoadEvent configLoadEvent = (DiscoverConfigLoadEvent) event;
            String dscrId = configLoadEvent.getDscrId();
            logger.info("[DiscoverConfigLoadEvent]handle event for {} version {}", dscrId, configLoadEvent.getVersion());
            ServiceDiscoveryWrapper wrapper = discoveryMap.get(dscrId);
            if (wrapper != null && wrapper.versionValidate(configLoadEvent.getVersion())) {
                logger.info("[DiscoverConfigLoadEvent]event for {}, version validate,no need to reload", dscrId);
                return;
            }
            try {
                this.loadDiscovery(dscrId, null);
            } catch (Exception e) {
                logger.error("[DiscoverConfigLoadEvent]error happened while handle event for {}", dscrId, e);
            }
        } else if (event instanceof DiscoverConfigDeleteEvent) {
            DiscoverConfigDeleteEvent deleteEvent = (DiscoverConfigDeleteEvent) event;
            String dscrId = deleteEvent.getDscrId();
            logger.info("[DiscoverConfigLoadEvent]handle event for {}", dscrId);
            this.discoveryDelete(dscrId);
        }
    }
}
