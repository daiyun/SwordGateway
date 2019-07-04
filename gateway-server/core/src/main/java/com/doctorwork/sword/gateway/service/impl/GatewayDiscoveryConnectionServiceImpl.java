package com.doctorwork.sword.gateway.service.impl;

import com.doctorwork.sword.gateway.dal.mapper.ext.ExtDiscoverConfigMapper;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtDiscoverRegistryConfigMapper;
import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.dal.model.DiscoverRegistryConfig;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:06 2019/7/4
 * @Modified By:
 */
@Service
public class GatewayDiscoveryConnectionServiceImpl implements GatewayDiscoveryConnectionService {
    private static final Logger logger = LoggerFactory.getLogger(GatewayDiscoveryConnectionServiceImpl.class);

    private GatewayDiscoveryConnectionService gatewayDiscoveryConnectionService;

    @Autowired
    private ExtDiscoverRegistryConfigMapper extDiscoverRegistryConfigMapper;

    @Override
    public DiscoverRegistryConfig get(String registryId){
        return extDiscoverRegistryConfigMapper.get(registryId);
    }
}
