package com.doctorwork.sword.gateway.admin.core.impl;

import com.doctorwork.sword.gateway.admin.core.GatewayAdminService;
import com.doctorwork.sword.gateway.admin.dal.mapper.ext.*;
import com.doctorwork.sword.gateway.admin.dal.model.*;
import com.doctorwork.sword.gateway.common.*;
import com.doctorwork.sword.gateway.common.RouteInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.doctorwork.sword.gateway.common.Constant.*;

/**
 * @Author:czq
 * @Description:
 * @Date: 18:00 2019/7/12
 * @Modified By:
 */
@Service
public class GatewayAdminServiceImpl implements GatewayAdminService {
    @Autowired
    private CuratorFramework curatorFramework;
    @Autowired
    private ExtLoadbalanceInfoMapper extLoadbalanceInfoMapper;

    @Autowired
    private ExtLoadbalanceServerMapper extLoadbalanceServerMapper;

    @Autowired
    private ExtDiscoverConfigMapper extDiscoverConfigMapper;

    @Autowired
    private ExtDiscoverRegistryConfigMapper extDiscoverRegistryConfigMapper;

    @Autowired
    private ExtRouteInfoMapper extRouteInfoMapper;

    @Autowired
    private ExtRoutePredicateMapper extRoutePredicateMapper;

    @Autowired
    private ExtRouteFilterMapper extRouteFilterMapper;


    @Override
    public void publishLoadBalanceConfig(String lbMark) throws Exception {
        LoadbalanceInfo loadbalanceInfo = extLoadbalanceInfoMapper.get(lbMark);
        if (loadbalanceInfo == null) {
            return;
        }
        LoadBalancerInfo loadBalancerInfo = new LoadBalancerInfo();
        loadBalancerInfo.setId(loadbalanceInfo.getLbMark());
        loadBalancerInfo.setDiscoveryId(loadbalanceInfo.getDscrId());
        loadBalancerInfo.setDscrEnable(loadbalanceInfo.getDscrEnable());
        loadBalancerInfo.setLbExtParam(loadbalanceInfo.getLbExtParam());
        loadBalancerInfo.setPingParam(loadbalanceInfo.getPingParam());
        loadBalancerInfo.setRuleParam(loadbalanceInfo.getRuleParam());
        loadBalancerInfo.setName(loadbalanceInfo.getLbName());
        loadBalancerInfo.setType(loadbalanceInfo.getLbType());

        curatorFramework.create()
                .orSetData()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(REGISTRY_PATH.concat(LOADBALANCE_NODE).concat(lbMark), JacksonUtil.toBytes(loadBalancerInfo));
    }

    @Override
    public void publishLoadBalanceServer(String lbMark) throws Exception {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        List<LoadbalanceServer> servers = extLoadbalanceServerMapper.getByLbMark(lbMark);
        for (LoadbalanceServer server : servers) {
            LoadBalancerServer loadBalancerServer = new LoadBalancerServer();
            loadBalancerServer.setLbId(server.getLbMark());
            loadBalancerServer.setSrvIp(server.getSrvIp());
            loadBalancerServer.setSrvName(server.getSrvName());
            loadBalancerServer.setSrvPort(server.getSrvPort());
            loadBalancerServer.setSrvWeight(server.getSrvWeight());
            curatorFramework.create()
                    .orSetData()
                    .creatingParentContainersIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(REGISTRY_PATH.concat(LOADBALANCE_SERVER_NODE).concat(lbMark).concat("/").concat(String.valueOf(server.getId())), JacksonUtil.toBytes(loadBalancerServer));
        }
    }

    @Override
    public void publishDiscoveryConfig(String dscrId) throws Exception {
        if (StringUtils.isEmpty(dscrId))
            return;
        DiscoverConfig discoverConfig = extDiscoverConfigMapper.get(dscrId);
        if (discoverConfig == null)
            return;
        DiscoveryInfo discoveryInfo = new DiscoveryInfo();
        discoveryInfo.setId(discoverConfig.getDscrId());
        discoveryInfo.setConectionId(discoverConfig.getDscrRegitryId());
        discoveryInfo.setConfig(discoverConfig.getDscrConfig());
        discoveryInfo.setPreload(discoverConfig.getDscrPreloadEnable());
        discoveryInfo.setType(discoverConfig.getDscrType());
        curatorFramework.create()
                .orSetData()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(REGISTRY_PATH.concat(DISCOVERY_NODE).concat(dscrId), JacksonUtil.toBytes(discoveryInfo));
    }

    @Override
    public void publishRegistryConfig(String registryId) throws Exception {
        DiscoverRegistryConfig discoverRegistryConfig = extDiscoverRegistryConfigMapper.get(registryId);
        if (discoverRegistryConfig == null)
            return;
        ConnectionInfo connectionInfo = new ConnectionInfo();
        connectionInfo.setId(discoverRegistryConfig.getDscrRegistryId());
        connectionInfo.setType(discoverRegistryConfig.getDscrRegistryType());
        connectionInfo.setConfig(discoverRegistryConfig.getDscrRegistryConfig());
        curatorFramework.create()
                .orSetData()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(REGISTRY_PATH.concat(REGISTRY_NODE).concat(registryId), JacksonUtil.toBytes(connectionInfo));
    }

    @Override
    public void publishRouteConfig(String routeMark) throws Exception {
        com.doctorwork.sword.gateway.admin.dal.model.RouteInfo tmp = extRouteInfoMapper.get(routeMark);
        if (tmp == null)
            return;
        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setRouteUri(tmp.getRouteUri());
        routeInfo.setRouteMark(tmp.getRouteMark());
        routeInfo.setRouteName(tmp.getRouteName());
        routeInfo.setRouteSort(tmp.getRouteSort());
        List<RoutePredicate> routePredicates = extRoutePredicateMapper.getByRoute(tmp.getId());
        List<RouteFilter> routeFilters = extRouteFilterMapper.getByRoute(tmp.getId());
        routeInfo.setPredications(routePredicates.stream().map(routePredicate -> {
            Predication predication = new Predication();
            predication.setRoutePredicateKey(routePredicate.getRoutePredicateKey());
            predication.setRoutePredicateValue(routePredicate.getRoutePredicateValue());
            return predication;
        }).collect(Collectors.toList()));
        routeInfo.setFilters(routeFilters.stream().map(routeFilter -> {
            FilterInfo filterInfo = new FilterInfo();
            filterInfo.setRouteFilterKey(routeFilter.getRouteFilterKey());
            filterInfo.setRouteFilterValue(routeFilter.getRouteFilterValue());
            return filterInfo;
        }).collect(Collectors.toList()));
        curatorFramework.create()
                .orSetData()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(REGISTRY_PATH.concat(ROUTE_NODE).concat(routeMark), JacksonUtil.toBytes(routeInfo));
    }
}
