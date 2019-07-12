package com.doctorwork.sword.gateway.discovery;

import com.doctorwork.sword.gateway.common.config.DiscoveryInfo;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import com.doctorwork.sword.gateway.discovery.connection.ServiceDiscoveryWrapper;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.Collection;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:27 2019/7/3
 * @Modified By:
 */
public interface IDiscoveryRepository {
    ServiceWrapper serviceWrapper(String serviceId);

    ServiceDiscoveryWrapper serviceDisovery(ServiceWrapper serviceWrapper);

    void loadService(String serviceId, DiscoveryInfo discoverConfig) throws Exception;

    void loadDiscovery(String dscrId, DiscoveryInfo discoverConfig) throws Exception;

    void loadRegistry(String registryId) throws Exception;

    void serviceDelete(String serviceId);

    Collection<ServiceInstance<ZookeeperInstance>> queryServices(String serviceId) throws Exception;
}
