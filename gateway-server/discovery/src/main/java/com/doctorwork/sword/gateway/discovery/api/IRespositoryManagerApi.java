package com.doctorwork.sword.gateway.discovery.api;

import com.doctorwork.sword.gateway.discovery.ServiceWrapper;
import com.doctorwork.sword.gateway.discovery.connection.IQueryService;
import com.doctorwork.sword.gateway.discovery.connection.ServiceDiscoveryWrapper;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:10 2019/7/4
 * @Modified By:
 */
public interface IRespositoryManagerApi {
    void connectionLoad(String registryId);

    void connectionClose(String registryId);

    ServiceWrapper serviceWrapper(String serviceId);

    ServiceDiscoveryWrapper serviceDisovery(ServiceWrapper serviceWrapper);

    void loadService(String serviceId);

    void loadDiscovery(String dscrId);

    void loadRegistry(String registryId);

    void serviceDelete(String serviceId);
}
