package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:29 2019/7/9
 * @Modified By:
 */
public interface IDiscoveryConfigRepository {
    DiscoverConfig discoveryConfig(String serviceId);
}
