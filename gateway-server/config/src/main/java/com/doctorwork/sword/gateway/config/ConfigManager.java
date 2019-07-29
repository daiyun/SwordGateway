package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.common.config.*;

import java.util.Collection;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:15 2019/7/11
 * @Modified By:
 */
public class ConfigManager extends AbstractConfiguration {
    private AbstractConfiguration configuration;

    public ConfigManager(GatewayConfig gatewayConfig, AbstractConfiguration configuration) {
        super(gatewayConfig);
        this.configuration = configuration;
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

    @Override
    public void init() throws Exception {
        configuration.init();
    }

    @Override
    public AbstractConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public RouteInfo routeInfo(String routeMark) {
        return getConfiguration().routeInfo(routeMark);
    }

    @Override
    public Collection<RouteInfo> routeInfos() {
        return getConfiguration().routeInfos();
    }
}
