package com.doctorwork.sword.gateway.service;

import com.doctorwork.sword.gateway.common.config.ConnectionInfo;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:05 2019/7/4
 * @Modified By:
 */
public interface GatewayDiscoveryConnectionService {
    ConnectionInfo get(String registryId);
}
