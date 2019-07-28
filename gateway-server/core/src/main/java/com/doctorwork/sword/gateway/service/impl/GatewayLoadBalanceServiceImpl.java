package com.doctorwork.sword.gateway.service.impl;

import com.doctorwork.sword.gateway.common.config.LoadBalancerInfo;
import com.doctorwork.sword.gateway.common.config.LoadBalancerServer;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtLoadbalanceInfoMapper;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtLoadbalanceServerMapper;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtServerInfoMapper;
import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.dal.model.LoadbalanceServer;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author chenzhiqiang
 * @date 2019/7/3
 */
@Service
public class GatewayLoadBalanceServiceImpl implements GatewayLoadBalanceService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayLoadBalanceServiceImpl.class);

    @Autowired
    private ExtLoadbalanceServerMapper extLoadbalanceServerMapper;

    @Autowired
    private ExtServerInfoMapper extServerInfoMapper;

    @Autowired
    private ExtLoadbalanceInfoMapper extLoadbalanceInfoMapper;

    @Override
    public List<LoadBalancerServer> loadBalanceServers(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            logger.error("无效的负载均衡标识{}", lbMark);
            return Collections.emptyList();
        }
        List<LoadbalanceServer> servers = extLoadbalanceServerMapper.get(lbMark);
        List<LoadBalancerServer> retList = new ArrayList<>(servers.size());
        for (LoadbalanceServer server : servers) {
            LoadBalancerServer loadBalancerServer = new LoadBalancerServer();
            loadBalancerServer.setLbId(server.getLbMark());
            loadBalancerServer.setSrvIp(server.getSrvIp());
            loadBalancerServer.setSrvName(server.getSrvName());
            loadBalancerServer.setSrvPort(server.getSrvPort());
            loadBalancerServer.setSrvWeight(server.getSrvWeight());
            loadBalancerServer.setSrvStatus(server.getSrvStatus());
            loadBalancerServer.setSrvEnable(server.getSrvEnable());
            retList.add(loadBalancerServer);
        }
        return retList;
    }

    @Override
    public LoadBalancerInfo loadBalance(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            logger.error("无效的负载均衡标识{}", lbMark);
            return null;
        }
        LoadbalanceInfo loadbalanceInfo = extLoadbalanceInfoMapper.get(lbMark);
        LoadBalancerInfo loadBalancerInfo = new LoadBalancerInfo();
        loadBalancerInfo.setId(loadbalanceInfo.getLbMark());
        loadBalancerInfo.setDiscoveryId(loadbalanceInfo.getDscrId());
        loadBalancerInfo.setDscrEnable(loadbalanceInfo.getDscrEnable());
        loadBalancerInfo.setLbExtParam(loadbalanceInfo.getLbExtParam());
        loadBalancerInfo.setPingParam(loadbalanceInfo.getPingParam());
        loadBalancerInfo.setRuleParam(loadbalanceInfo.getRuleParam());
        loadBalancerInfo.setName(loadbalanceInfo.getLbName());
        loadBalancerInfo.setType(loadbalanceInfo.getLbType());
        return loadBalancerInfo;
    }
}
