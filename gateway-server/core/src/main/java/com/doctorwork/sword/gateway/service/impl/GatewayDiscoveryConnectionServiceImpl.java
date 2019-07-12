package com.doctorwork.sword.gateway.service.impl;

import com.doctorwork.sword.gateway.common.config.ConnectionInfo;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtDiscoverRegistryConfigMapper;
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

    @Autowired
    private ExtDiscoverRegistryConfigMapper extDiscoverRegistryConfigMapper;

    @Override
    public ConnectionInfo get(String registryId) {
        DiscoverRegistryConfig discoverRegistryConfig = extDiscoverRegistryConfigMapper.get(registryId);
        if (discoverRegistryConfig == null)
            return null;
        ConnectionInfo connectionInfo = new ConnectionInfo();
        connectionInfo.setId(discoverRegistryConfig.getDscrRegistryId());
        connectionInfo.setType(discoverRegistryConfig.getDscrRegistryType());
        connectionInfo.setConfig(discoverRegistryConfig.getDscrRegistryConfig());
        return connectionInfo;
    }
}
