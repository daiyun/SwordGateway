package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.dal.model.DiscoverRegistryConfig;
import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryConnectionService;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryService;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:05 2019/7/11
 * @Modified By:
 */
public class DataBaseConfigRepository extends AbstractConfiguration {
    private GatewayDiscoveryService gatewayDiscoveryService;
    private GatewayDiscoveryConnectionService gatewayDiscoveryConnectionService;
    private GatewayLoadBalanceService gatewayLoadBalanceService;

    public DataBaseConfigRepository(GatewayLoadBalanceService gatewayLoadBalanceService, GatewayDiscoveryService gatewayDiscoveryService, GatewayDiscoveryConnectionService gatewayDiscoveryConnectionService, GatewayConfig gatewayConfig) {
        super(gatewayConfig);
        this.gatewayDiscoveryConnectionService = gatewayDiscoveryConnectionService;
        this.gatewayDiscoveryService = gatewayDiscoveryService;
        this.gatewayLoadBalanceService = gatewayLoadBalanceService;
    }

    @Override
    public DiscoverRegistryConfig connectionConfig(String registryId) {
        return gatewayDiscoveryConnectionService.get(registryId);
    }

    @Override
    public DiscoverConfig discoveryConfig(String dscrId) {
        return gatewayDiscoveryService.discoverConfig(dscrId);
    }

    @Override
    public DiscoverConfig discoveryConfigFromLoadBalance(String lbMark) {
        LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(lbMark);
        if (loadbalanceInfo == null || loadbalanceInfo.getDscrEnable().equals(0) || StringUtils.isEmpty(loadbalanceInfo.getDscrId()))
            return null;
        return gatewayDiscoveryService.discoverConfig(loadbalanceInfo.getDscrId());
    }

    @Override
    public LoadbalanceInfo loadbalanceConfig(String lbMark) {
        return gatewayLoadBalanceService.loadBalance(lbMark);
    }

    @Override
    public AbstractConfiguration getConfiguration() {
        return this;
    }
}
