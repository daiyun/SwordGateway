package com.doctorwork.sword.gateway.common.config;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:44 2019/7/10
 * @Modified By:
 */
public interface IConnectionConfigRepository {
    ConnectionInfo connectionConfig(String registryId);
}
