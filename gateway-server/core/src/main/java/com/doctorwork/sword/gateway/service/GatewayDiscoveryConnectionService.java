package com.doctorwork.sword.gateway.service;

import com.doctorwork.sword.gateway.dal.model.DiscoverRegistryConfig;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:05 2019/7/4
 * @Modified By:
 */
public interface GatewayDiscoveryConnectionService {
    DiscoverRegistryConfig get(String registryId);
}
