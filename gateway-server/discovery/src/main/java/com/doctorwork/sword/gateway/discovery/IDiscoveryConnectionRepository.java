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

    void connectionLoad(String registryId, IDiscoveryRepository discoveryRepository) throws IOException;

    void connectionClose(String registryId) throws IOException;
}
