package com.doctorwork.sword.gateway.config;

import com.doctorwork.com.sword.gateway.registry.IRegistryConnectionRepository;
import com.doctorwork.sword.gateway.common.event.AbstractEvent;
import com.doctorwork.sword.gateway.common.event.EventPost;
import com.doctorwork.sword.gateway.common.listener.EventListener;
import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.dal.model.DiscoverRegistryConfig;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryService;
import com.google.common.eventbus.EventBus;

/**
 * @author chenzhiqiang
 * @date 2019/7/8
 */
public class ConfigurationRepository extends AbstractConfiguration implements EventPost, EventListener {
    private GatewayDiscoveryService gatewayDiscoveryService;
    private IRegistryConnectionRepository registryConnectionRepository;
    private EventBus eventBus;

    public ConfigurationRepository(GatewayDiscoveryService gatewayDiscoveryService, IRegistryConnectionRepository registryConnectionRepository, EventBus eventBus) {
        this.gatewayDiscoveryService = gatewayDiscoveryService;
        this.registryConnectionRepository = registryConnectionRepository;
        this.eventBus = eventBus;
        register(this.eventBus);
    }


    @Override
    public void eventPost(AbstractEvent event) {

    }

    @Override
    public void handleEvent(AbstractEvent event) {

    }

    @Override
    public DiscoverRegistryConfig connectionConfigGet(String registryId) {
        return null;
    }

    @Override
    public DiscoverConfig discoveryConfig(String serviceId) {
        return null;
    }
}
