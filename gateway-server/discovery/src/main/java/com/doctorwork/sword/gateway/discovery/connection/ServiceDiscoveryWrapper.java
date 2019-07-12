package com.doctorwork.sword.gateway.discovery.connection;

import com.doctorwork.sword.gateway.discovery.IQueryService;
import com.doctorwork.sword.gateway.discovery.common.Constants;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * @Author:czq
 * @Description:
 * @Date: 12:36 2019/7/4
 * @Modified By:
 */
public class ServiceDiscoveryWrapper implements Closeable, IQueryService {
    private final ServiceDiscovery serviceDiscovery;
    private final String id;
    private final String connectionId;
    private Integer version;

    public ServiceDiscoveryWrapper(ServiceDiscovery serviceDiscovery, String id, String connectionId, Integer version) {
        this.id = id;
        this.connectionId = connectionId;
        this.version = version;
        if (serviceDiscovery == null)
            throw new RuntimeException("service discovery must not be null");
        this.serviceDiscovery = serviceDiscovery;
    }

    public String getId() {
        return id;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public boolean versionValidate(Integer version) {
        return Objects.equals(this.version, version);
    }

    @Override
    public void close() throws IOException {
        serviceDiscovery.close();
    }

    public ServiceCache<ZookeeperInstance> serviceCache(String name) {
        return serviceDiscovery.serviceCacheBuilder().name(name.concat(Constants.PROVIDES_PATH)).build();
    }

    @Override
    public Collection<ServiceInstance<ZookeeperInstance>> getInstances(String name) throws Exception {
        Collection<ServiceInstance<ZookeeperInstance>> instances = serviceDiscovery
                .queryForInstances(name.concat(Constants.PROVIDES_PATH));
        if (instances == null || instances.isEmpty()) {
            return Collections.emptyList();
        }
        return instances;
    }
}
