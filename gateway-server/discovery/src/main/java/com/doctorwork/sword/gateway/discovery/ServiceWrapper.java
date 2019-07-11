package com.doctorwork.sword.gateway.discovery;

import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.doctorwork.sword.gateway.discovery.connection.ServiceDiscoveryWrapper;
import com.doctorwork.sword.gateway.common.event.EventPost;
import com.doctorwork.sword.gateway.common.event.ServiceCacheChangeEvent;
import org.apache.curator.x.discovery.ServiceCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenzhiqiang
 * @date 2019/7/3
 */
public class ServiceWrapper extends CacheListener {

    private static final Logger logger = LoggerFactory.getLogger(ServiceWrapper.class);

    private String serviceId;
    private String dscrMapKey;
    private ServiceCache<ZookeeperInstance> serviceCache;
    private IDiscoveryRepository iDiscoveryRepository;
    private EventPost eventPost;

    public ServiceWrapper(String serviceId, String dscrMapKey, IDiscoveryRepository iDiscoveryRepository, EventPost eventPost) {
        if (StringUtils.isEmpty(serviceId) || iDiscoveryRepository == null)
            throw new RuntimeException("serviceId or IDiscoveryRepository must not be null");
        this.serviceId = serviceId;
        this.dscrMapKey = dscrMapKey;
        this.iDiscoveryRepository = iDiscoveryRepository;
        this.eventPost = eventPost;
        this.buildServiceCache();
    }

    private void buildServiceCache() {
        closeCache();
        ServiceDiscoveryWrapper serviceDiscoveryWrapper = serviceDiscovery();
        if (serviceDiscoveryWrapper == null) {
            logger.error("service[{}] discovery cache build failed,because the service discovery is null", this.serviceId);
            return;
        }
        try {
            ServiceCache<ZookeeperInstance> serviceCache = serviceDiscoveryWrapper.serviceCache(serviceId);
            serviceCache.start();
            serviceCache.addListener(this);
            this.serviceCache = serviceCache;
        } catch (Exception e) {
            logger.error("error create servicecache for {}", serviceId, e);
        }
    }

    public void reload(String dscrMapKey) {
        String oldMapKey = this.dscrMapKey;
        logger.info("reload servicewrapper from {} to {}", oldMapKey, dscrMapKey);
        this.dscrMapKey = dscrMapKey;
        this.buildServiceCache();
    }

    public void reloadCache() {
        logger.info("reload servicewrapper cache for {}", dscrMapKey);
        this.buildServiceCache();
    }

    public String getDscrMapKey() {
        return dscrMapKey;
    }

    private ServiceDiscoveryWrapper serviceDiscovery() {
        return iDiscoveryRepository.serviceDisovery(this);
    }

    public IQueryService queryService() {
        return this.serviceDiscovery();
    }

    public void clear() {
        this.iDiscoveryRepository = null;
        this.dscrMapKey = null;
        this.serviceId = null;
        closeCache();
    }

    private void closeCache() {
        if (serviceCache != null) {
            serviceCache.removeListener(this);
            try {
                serviceCache.close();
            } catch (Exception e) {
                logger.error("error close servicecache for {}", serviceId, e);
            } finally {
                serviceCache = null;
            }

        }
    }

    @Override
    public void serviceReload() {
        String serviceId = this.serviceId;
        String dscrMapKey = this.dscrMapKey;
        eventPost.eventPost(new ServiceCacheChangeEvent(serviceId, dscrMapKey));
    }
}
