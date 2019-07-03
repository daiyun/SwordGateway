package com.doctorwork.sword.gateway.loadbalance;

import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.details.ServiceCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author chenzhiqiang
 * @date 2019/7/3
 */
public class ServiceDiscoveryWrapper {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscoveryWrapper.class);

    private String serviceId;
    private String dscrMapKey;
    private ServiceCache<ZookeeperInstance> serviceCache;
    private IDiscoveryRepository iDiscoveryRepository;

    public ServiceDiscoveryWrapper(String serviceId, String dscrMapKey, IDiscoveryRepository iDiscoveryRepository) {
        if (StringUtils.isEmpty(serviceId) || iDiscoveryRepository == null)
            throw new RuntimeException("serviceId or IDiscoveryRepository must not be null");
        this.serviceId = serviceId;
        this.dscrMapKey = dscrMapKey;
        this.iDiscoveryRepository = iDiscoveryRepository;
        this.buildServiceCache();
    }

    private void buildServiceCache() {
        if(serviceCache != null){
            try {
                serviceCache.close();
            } catch (IOException e) {
                logger.error("error close servicecache for {}", serviceId, e);
            }
        }
        ServiceDiscovery<ZookeeperInstance> serviceDiscovery = serviceDiscovery();
        if (serviceDiscovery == null) {
            logger.error("service[{}] discovery cache build failed,because the service discovery is null", this.serviceId);
            return;
        }
        try {
            ServiceCache<ZookeeperInstance> serviceCache = serviceDiscovery.serviceCacheBuilder().name(serviceId).build();
            serviceCache.start();
            serviceCache.addListener(new ServiceCacheListener() {
                @Override
                public void cacheChanged() {
                    logger.warn("节点配置变更");
                }

                @Override
                public void stateChanged(CuratorFramework client, ConnectionState newState) {
                    logger.warn("节点连接状态变更");
                }
            });
            this.serviceCache = serviceCache;
        } catch (Exception e) {
            logger.error("error create servicecache for {}", serviceId, e);
        }
    }

    public void reload(String dscrMapKey) {
        this.dscrMapKey = dscrMapKey;
        this.buildServiceCache();
    }

    public void reloadCache() {
        this.buildServiceCache();
    }

    public String getDscrMapKey() {
        return dscrMapKey;
    }

    public ServiceDiscovery<ZookeeperInstance> serviceDiscovery() {
        return iDiscoveryRepository.serviceDisovery(this.serviceId);
    }

    public void clear() {
        this.serviceCache = null;
        this.iDiscoveryRepository = null;
        this.dscrMapKey = null;
        this.serviceId = null;
    }
}
