package com.doctorwork.sword.gateway.config;

import com.doctorwork.com.sword.gateway.registry.IRegistryConnectionRepository;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryService;

/**
 * @author chenzhiqiang
 * @date 2019/7/8
 */
public class DiscoveryConfigurationManager implements IDiscoveryConfigManager {
    private GatewayDiscoveryService gatewayDiscoveryService;
    private IRegistryConnectionRepository registryConnectionRepository;

    public DiscoveryConfigurationManager(GatewayDiscoveryService gatewayDiscoveryService, IRegistryConnectionRepository registryConnectionRepository) {
        this.gatewayDiscoveryService = gatewayDiscoveryService;
        this.registryConnectionRepository = registryConnectionRepository;
    }
}
