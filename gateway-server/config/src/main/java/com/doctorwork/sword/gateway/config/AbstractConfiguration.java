package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.common.config.IConnectionConfigRepository;
import com.doctorwork.sword.gateway.common.config.IDiscoveryConfigRepository;
import com.doctorwork.sword.gateway.common.config.ILoadBalancerConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:46 2019/7/10
 * @Modified By:
 */
public abstract class AbstractConfiguration implements IDiscoveryConfigRepository, IConnectionConfigRepository, ILoadBalancerConfigRepository {
    protected static Logger logger = LoggerFactory.getLogger(AbstractConfiguration.class);
    private GatewayConfig gatewayConfig;

    protected AbstractConfiguration(GatewayConfig gatewayConfig) {
        if (gatewayConfig == null)
            throw new RuntimeException("gateway config must not be null");
        this.gatewayConfig = gatewayConfig;
    }

    public void setGatewayConfig(GatewayConfig gatewayConfig) {
        this.gatewayConfig = gatewayConfig;
    }

    public GatewayConfig getGatewayConfig() {
        return gatewayConfig;
    }

    public abstract AbstractConfiguration getConfiguration();
}
