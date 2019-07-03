package com.doctorwork.sword.gateway.loadbalance;

import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import org.apache.curator.x.discovery.ServiceDiscovery;

import java.io.IOException;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:27 2019/7/3
 * @Modified By:
 */
public interface IDiscoveryRepository {
    ServiceDiscoveryWrapper serviceWrapper(String serviceId);

    ServiceDiscovery<ZookeeperInstance> serviceDisovery(String serviceId);

    void reload();

    void loadService(String serviceId, DiscoverConfig discoverConfig);

    void loadDiscovery(String dscrId, DiscoverConfig discoverConfig) throws IOException;

    void delete(String serviceId);
}
