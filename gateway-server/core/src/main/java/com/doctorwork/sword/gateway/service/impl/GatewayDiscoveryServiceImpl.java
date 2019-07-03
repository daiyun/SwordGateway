package com.doctorwork.sword.gateway.service.impl;

import com.doctorwork.sword.gateway.dal.mapper.ext.ExtDiscoverConfigMapper;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtDiscoverLoadbalancePoolMapper;
import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.dal.model.DiscoverLoadbalancePool;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chenzhiqiang
 * @date 2019/7/3
 */
@Service
public class GatewayDiscoveryServiceImpl implements GatewayDiscoveryService {
    @Autowired
    private ExtDiscoverConfigMapper extDiscoverConfigMapper;
    @Autowired
    private ExtDiscoverLoadbalancePoolMapper extDiscoverLoadbalancePoolMapper;

    @Override
    public DiscoverConfig serviceTodiscoverConfig(String serviceId) {
        if (StringUtils.isEmpty(serviceId))
            return null;
        DiscoverLoadbalancePool pool = extDiscoverLoadbalancePoolMapper.get(serviceId);
        if (pool == null)
            return null;
        DiscoverConfig discoverConfig = extDiscoverConfigMapper.get(pool.getDscrId());
        return discoverConfig;
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
        if (CollectionUtils.isEmpty(discoverConfigs))
            return Collections.emptyMap();
        Map<String, DiscoverConfig> discoverConfigMap = discoverConfigs.stream().collect(Collectors.toMap(DiscoverConfig::getDscrId, Function.identity(), (k1, k2) -> k1));
        List<DiscoverLoadbalancePool> pools = extDiscoverLoadbalancePoolMapper.list(discoverConfigMap.keySet());
        if (CollectionUtils.isEmpty(pools))
            return Collections.emptyMap();
        return pools.stream().collect(Collectors.toMap(DiscoverLoadbalancePool::getLbMark,
                discoverLoadbalancePool -> discoverConfigMap.get(discoverLoadbalancePool.getDscrId()), (k1, k2) -> k1));
    }
}
