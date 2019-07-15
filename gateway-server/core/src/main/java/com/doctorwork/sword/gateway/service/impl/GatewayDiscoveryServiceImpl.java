package com.doctorwork.sword.gateway.service.impl;

import com.doctorwork.sword.gateway.common.config.DiscoveryInfo;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtDiscoverConfigMapper;
import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
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
    public DiscoveryInfo discoverConfig(String dscrId) {
        if (StringUtils.isEmpty(dscrId))
            return null;
        DiscoverConfig discoverConfig = extDiscoverConfigMapper.get(dscrId);
        if(discoverConfig == null)
            return null;
        DiscoveryInfo discoveryInfo = new DiscoveryInfo();
        discoveryInfo.setId(discoverConfig.getDscrId());
        discoveryInfo.setConectionId(discoverConfig.getDscrRegitryId());
        discoveryInfo.setConfig(discoverConfig.getDscrConfig());
        discoveryInfo.setPreload(discoverConfig.getDscrPreloadEnable());
        discoveryInfo.setType(discoverConfig.getDscrType());
        return discoveryInfo;
    }

    @Override
    public List<DiscoveryInfo> all() {
        List<DiscoverConfig> discoverConfigs = extDiscoverConfigMapper.all();
        if(CollectionUtils.isEmpty(discoverConfigs))
            return Collections.emptyList();
        List<DiscoveryInfo> discoveryInfos = new ArrayList<>(discoverConfigs.size());
        for (DiscoverConfig discoverConfig : discoverConfigs) {
            DiscoveryInfo discoveryInfo = new DiscoveryInfo();
            discoveryInfo.setId(discoverConfig.getDscrId());
            discoveryInfo.setConectionId(discoverConfig.getDscrRegitryId());
            discoveryInfo.setConfig(discoverConfig.getDscrConfig());
            discoveryInfo.setPreload(discoverConfig.getDscrPreloadEnable());
            discoveryInfo.setType(discoverConfig.getDscrType());
        }
        return discoveryInfos;
    }

    @Override
    public Map<String, DiscoverConfig> poolMap(List<DiscoverConfig> discoverConfigs) {
        return null;
    }
}
