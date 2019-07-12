package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.common.config.ConnectionInfo;
import com.doctorwork.sword.gateway.common.config.DiscoveryInfo;
import com.doctorwork.sword.gateway.common.config.LoadBalancerInfo;
import com.doctorwork.sword.gateway.common.config.LoadBalancerServer;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryConnectionService;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryService;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;

import java.util.Collection;

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
    public ConnectionInfo connectionConfig(String registryId) {
        return gatewayDiscoveryConnectionService.get(registryId);
    }

    @Override
    public DiscoveryInfo discoveryConfig(String dscrId) {
        return gatewayDiscoveryService.discoverConfig(dscrId);
    }

    @Override
    public DiscoveryInfo discoveryConfigFromLoadBalance(String lbMark) {
        LoadBalancerInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(lbMark);
        if (loadbalanceInfo == null || loadbalanceInfo.getDscrEnable().equals(0) || StringUtils.isEmpty(loadbalanceInfo.getDiscoveryId()))
            return null;
        return gatewayDiscoveryService.discoverConfig(loadbalanceInfo.getDiscoveryId());
    }

    @Override
    public LoadBalancerInfo loadbalanceConfig(String lbMark) {
        return gatewayLoadBalanceService.loadBalance(lbMark);
    }

    @Override
    public Collection<LoadBalancerServer> loadbalanceServer(String lbMark) {
        return gatewayLoadBalanceService.loadBalanceServers(lbMark);
    }

    @Override
    public AbstractConfiguration getConfiguration() {
        return this;
    }
}
