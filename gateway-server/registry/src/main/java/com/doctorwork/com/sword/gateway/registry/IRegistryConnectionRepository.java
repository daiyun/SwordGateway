package com.doctorwork.com.sword.gateway.registry;

import com.doctorwork.com.sword.gateway.registry.wrapper.ConnectionWrapper;
import com.doctorwork.sword.gateway.common.config.IConnectionConfigRepository;

import java.io.IOException;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:25 2019/7/4
 * @Modified By:
 */
public interface IRegistryConnectionRepository {
    ConnectionWrapper connection(String registryId);

    void connectionLoad(String registryId) throws IOException;

    void connectionClose(String registryId) throws IOException;

    void setConnectionConfig(IConnectionConfigRepository connectionConfigRepository);
}
