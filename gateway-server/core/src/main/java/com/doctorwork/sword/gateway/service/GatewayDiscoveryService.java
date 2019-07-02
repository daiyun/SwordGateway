package com.doctorwork.sword.gateway.service;

import com.doctorwork.sword.gateway.dal.mapper.ext.ExtDiscoverConfigMapper;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtDiscoverLoadbalancePoolMapper;
import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.dal.model.DiscoverLoadbalancePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author:czq
 * @Description:
 * @Date: 14:27 2019/7/2
 * @Modified By:
 */
@Service
public class GatewayDiscoveryService {
    @Autowired
    private ExtDiscoverConfigMapper extDiscoverConfigMapper;
    @Autowired
    private ExtDiscoverLoadbalancePoolMapper extDiscoverLoadbalancePoolMapper;

    public DiscoverConfig discoverConfig(String serviceId) {
        DiscoverLoadbalancePool pool = extDiscoverLoadbalancePoolMapper.get(serviceId);
        if (pool == null)
            return null;
        DiscoverConfig discoverConfig = extDiscoverConfigMapper.get(pool.getDscrId());
        return discoverConfig;
    }

    public List<DiscoverConfig> preLoadList() {
        return extDiscoverConfigMapper.preLoad();
    }

    public Map<String, String> poolMap(List<DiscoverConfig> discoverConfigs) {
        if (CollectionUtils.isEmpty(discoverConfigs))
            return Collections.emptyMap();
        Set<String> dscrIds = discoverConfigs.stream().map(DiscoverConfig::getDscrId).collect(Collectors.toSet());
        List<DiscoverLoadbalancePool> pools = extDiscoverLoadbalancePoolMapper.list(dscrIds);
        if (CollectionUtils.isEmpty(pools))
            return Collections.emptyMap();
        return pools.stream().collect(Collectors.toMap(DiscoverLoadbalancePool::getLbMark, DiscoverLoadbalancePool::getDscrId, (k1, k2) -> k1));
    }
}
