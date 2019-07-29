package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.common.config.*;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryConnectionService;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryService;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;
import com.doctorwork.sword.gateway.service.GatewayRouteService;

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
    private GatewayRouteService gatewayRouteService;

    public DataBaseConfigRepository(GatewayLoadBalanceService gatewayLoadBalanceService,
                                    GatewayDiscoveryService gatewayDiscoveryService,
                                    GatewayDiscoveryConnectionService gatewayDiscoveryConnectionService,
                                    GatewayConfig gatewayConfig, GatewayRouteService gatewayRouteService) {
        super(gatewayConfig);
        this.gatewayDiscoveryConnectionService = gatewayDiscoveryConnectionService;
        this.gatewayDiscoveryService = gatewayDiscoveryService;
        this.gatewayLoadBalanceService = gatewayLoadBalanceService;
        this.gatewayRouteService = gatewayRouteService;
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
    public Collection<DiscoveryInfo> all() {
        return gatewayDiscoveryService.all();
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
    public RouteInfo routeInfo(String routeMark) {
        return gatewayRouteService.routeDefinition(routeMark);
    }

    @Override
    public Collection<RouteInfo> routeInfos() {
        return gatewayRouteService.routeDefinitions();
    }

    @Override
    public void init() {

    }

    @Override
    public AbstractConfiguration getConfiguration() {
        return this;
    }
}
