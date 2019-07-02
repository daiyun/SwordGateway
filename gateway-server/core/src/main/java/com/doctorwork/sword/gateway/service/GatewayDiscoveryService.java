package com.doctorwork.sword.gateway.service;

import com.doctorwork.sword.gateway.dal.mapper.ext.ExtDiscoverConfigMapper;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtDiscoverLoadbalancePoolMapper;
import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.dal.model.DiscoverLoadbalancePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
