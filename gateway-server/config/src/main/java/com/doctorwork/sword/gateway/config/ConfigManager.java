package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.common.config.ConnectionInfo;
import com.doctorwork.sword.gateway.common.config.DiscoveryInfo;
import com.doctorwork.sword.gateway.common.config.LoadBalancerInfo;
import com.doctorwork.sword.gateway.common.config.LoadBalancerServer;

import java.util.Collection;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:15 2019/7/11
 * @Modified By:
 */
public class ConfigManager extends AbstractConfiguration {
    private DataBaseConfigRepository dataBaseConfigRepository;
    private RegistryConfigRepository registryConfigRepository;

    public ConfigManager(GatewayConfig gatewayConfig, DataBaseConfigRepository dataBaseConfigRepository, RegistryConfigRepository registryConfigRepository) {
        super(gatewayConfig);
        this.dataBaseConfigRepository = dataBaseConfigRepository;
        this.registryConfigRepository = registryConfigRepository;
    }

    @Override
    public ConnectionInfo connectionConfig(String registryId) {
        return getConfiguration().connectionConfig(registryId);
    }

    @Override
    public DiscoveryInfo discoveryConfig(String dscrId) {
        return getConfiguration().discoveryConfig(dscrId);
    }

    @Override
    public DiscoveryInfo discoveryConfigFromLoadBalance(String lbMark) {
        return getConfiguration().discoveryConfigFromLoadBalance(lbMark);
    }

    @Override
    public Collection<DiscoveryInfo> all() {
        return getConfiguration().all();
    }

    @Override
    public LoadBalancerInfo loadbalanceConfig(String lbMark) {
        return getConfiguration().loadbalanceConfig(lbMark);
    }

    @Override
    public Collection<LoadBalancerServer> loadbalanceServer(String lbMark) {
        return getConfiguration().loadbalanceServer(lbMark);
    }

    public void init() throws Exception {
        if (getGatewayConfig().isUseRegistry()) {
            registryConfigRepository.init();
        }
    }

    @Override
    public AbstractConfiguration getConfiguration() {
        if (getGatewayConfig().isUseRegistry()) {
            return registryConfigRepository;
        }
        return dataBaseConfigRepository;
    }
}
