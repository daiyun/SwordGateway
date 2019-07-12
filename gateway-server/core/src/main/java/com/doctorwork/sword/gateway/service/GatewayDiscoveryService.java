package com.doctorwork.sword.gateway.service;

import com.doctorwork.sword.gateway.common.config.DiscoveryInfo;
import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;

import java.util.List;
import java.util.Map;

/**
 * @Author:czq
 * @Description:
 * @Date: 14:27 2019/7/2
 * @Modified By:
 */
public interface GatewayDiscoveryService {

    DiscoverConfig serviceTodiscoverConfig(String serviceId);

    DiscoveryInfo discoverConfig(String dscrId);

    List<DiscoverConfig> preLoadList();

    Map<String, DiscoverConfig> poolMap(List<DiscoverConfig> discoverConfigs);
}
