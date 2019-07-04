package com.doctorwork.sword.gateway.discovery;

import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.discovery.connection.ServiceDiscoveryWrapper;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:27 2019/7/3
 * @Modified By:
 */
public interface IDiscoveryRepository {
    ServiceWrapper serviceWrapper(String serviceId);

    ServiceDiscoveryWrapper serviceDisovery(ServiceWrapper serviceWrapper);

    void reload();

    void loadService(String serviceId, DiscoverConfig discoverConfig) throws Exception;

    void loadDiscovery(String dscrId, DiscoverConfig discoverConfig) throws Exception;

    void loadRegistry(String registryId) throws Exception;

    void delete(String serviceId);
}
