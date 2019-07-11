package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.dal.model.DiscoverRegistryConfig;
import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;

import java.io.IOException;

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
    public DiscoverRegistryConfig connectionConfig(String registryId) {
        return getConfiguration().connectionConfig(registryId);
    }

    @Override
    public DiscoverConfig discoveryConfig(String dscrId) {
        return getConfiguration().discoveryConfig(dscrId);
    }

    @Override
    public DiscoverConfig discoveryConfigFromLoadBalance(String lbMark) {
        return getConfiguration().discoveryConfigFromLoadBalance(lbMark);
    }

    @Override
    public LoadbalanceInfo loadbalanceConfig(String lbMark) {
        return getConfiguration().loadbalanceConfig(lbMark);
    }

    @Override
    public AbstractConfiguration getConfiguration() {
        if (getGatewayConfig().isUseRegistry()) {
            try {
                registryConfigRepository.init();
            } catch (IOException e) {
                logger.error("registry config init error ", e);
            }
            return registryConfigRepository;
        }
        return dataBaseConfigRepository;
    }
}
