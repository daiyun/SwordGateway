package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.dal.model.DiscoverRegistryConfig;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:44 2019/7/10
 * @Modified By:
 */
public interface IConnectionConfigRepository {
    DiscoverRegistryConfig connectionConfig(String registryId);
}
