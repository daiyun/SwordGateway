package com.doctorwork.sword.gateway.service.impl;

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
    public List<LoadbalanceServer> loadBalanceServers(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            logger.error("无效的负载均衡标识{}", lbMark);
            return Collections.emptyList();
        }
        return extLoadbalanceServerMapper.get(lbMark);
    }

    @Override
    public LoadbalanceInfo loadBalance(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            logger.error("无效的负载均衡标识{}", lbMark);
            return null;
        }
        return extLoadbalanceInfoMapper.get(lbMark);
    }
}
