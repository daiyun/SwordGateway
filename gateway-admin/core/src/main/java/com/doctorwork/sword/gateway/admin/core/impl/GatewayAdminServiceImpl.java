package com.doctorwork.sword.gateway.admin.core.impl;

import com.doctorwork.sword.gateway.admin.core.GatewayAdminService;
import com.doctorwork.sword.gateway.common.*;
import com.doctorwork.sword.gateway.admin.dal.mapper.ext.ExtDiscoverConfigMapper;
import com.doctorwork.sword.gateway.admin.dal.mapper.ext.ExtDiscoverRegistryConfigMapper;
import com.doctorwork.sword.gateway.admin.dal.mapper.ext.ExtLoadbalanceInfoMapper;
import com.doctorwork.sword.gateway.admin.dal.mapper.ext.ExtLoadbalanceServerMapper;
import com.doctorwork.sword.gateway.admin.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.admin.dal.model.DiscoverRegistryConfig;
import com.doctorwork.sword.gateway.admin.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.admin.dal.model.LoadbalanceServer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

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
}
