package com.doctorwork.sword.gateway.discovery;

import com.doctorwork.sword.gateway.discovery.connection.DiscoveryConnectionWrapper;

import java.io.IOException;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:25 2019/7/4
 * @Modified By:
 */
public interface IDiscoveryConnectionRepository {
    DiscoveryConnectionWrapper connection(String registryId);

    void load(String registryId, IDiscoveryRepository discoveryRepository) throws IOException;

    void close(String registryId) throws IOException;
}
