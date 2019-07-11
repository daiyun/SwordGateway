package com.doctorwork.sword.gateway.service.impl;

import com.doctorwork.sword.gateway.dal.mapper.ext.ExtDiscoverConfigMapper;
import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhiqiang
 * @date 2019/7/3
 */
@Service
public class GatewayDiscoveryServiceImpl implements GatewayDiscoveryService {
    @Autowired
    private ExtDiscoverConfigMapper extDiscoverConfigMapper;

    @Override
    public DiscoverConfig serviceTodiscoverConfig(String serviceId) {
        return null;
    }

    @Override
    public DiscoverConfig discoverConfig(String dscrId) {
        if (StringUtils.isEmpty(dscrId))
            return null;
        DiscoverConfig discoverConfig = extDiscoverConfigMapper.get(dscrId);
        return discoverConfig;
    }

    @Override
    public List<DiscoverConfig> preLoadList() {
        return extDiscoverConfigMapper.preLoad();
    }

    @Override
    public Map<String, DiscoverConfig> poolMap(List<DiscoverConfig> discoverConfigs) {
        return null;
    }
}
