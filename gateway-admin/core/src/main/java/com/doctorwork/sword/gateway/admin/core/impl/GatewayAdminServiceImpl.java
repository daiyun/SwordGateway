package com.doctorwork.sword.gateway.admin.core.impl;

import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.common.LoadBalancerInfo;
import com.doctorwork.sword.gateway.admin.core.GatewayAdminService;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtLoadbalanceInfoMapper;
import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private static final String REGISTRY_PATH = "/doctorwork/gateway/configuration";
    private static final String REGISTRY_NODE = "/registry/";
    private static final String LOADBALANCE_NODE = "/loadbalance/";
    private static final String DISCOVERY_NODE = "/discovery/";
    private static final String LOADBALANCE_SERVER_NODE = "/loadbalance-server/";

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

        curatorFramework.create().orSetData().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(REGISTRY_PATH.concat(LOADBALANCE_NODE).concat(lbMark), JacksonUtil.toBytes(loadBalancerInfo));
    }
}
