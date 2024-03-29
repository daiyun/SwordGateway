package com.doctorwork.sword.gateway.discovery.config;

import com.doctorwork.com.sword.gateway.registry.IRegistryConnectionRepository;
import com.doctorwork.com.sword.gateway.registry.wrapper.ConnectionWrapper;
import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.common.config.DiscoveryInfo;
import com.doctorwork.sword.gateway.discovery.common.DiscoveryProperties;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import com.doctorwork.sword.gateway.discovery.common.builder.DiscoveryBuilder;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.doctorwork.sword.gateway.discovery.connection.ServiceDiscoveryWrapper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;

/**
 * @Author:czq
 * @Description:
 * @Date: 14:39 2019/7/2
 * @Modified By:
 */
public class DiscoveryConfig<T> {
    private final String dscrId;
    private final Boolean preLoad;
    private final String mapperRegistryKey;
    private final T properties;
    private final Integer version;

    public static DiscoveryConfig build(DiscoveryInfo discoveryInfo) {
        DiscoveryProperties discoveryProperties = JacksonUtil.toObject(discoveryInfo.getConfig(), DiscoveryProperties.class);
        DiscoveryConfig<DiscoveryProperties> discoveryConfig = new DiscoveryConfig<>(discoveryInfo.getId(),
                discoveryInfo.getPreload() == 0, discoveryInfo.getConectionId(), discoveryProperties, discoveryInfo.getVersion());
        return discoveryConfig;
    }

    public DiscoveryConfig(String dscrId, Boolean preLoad, String mapperRegistryKey, T properties, Integer version) {
        this.dscrId = dscrId;
        this.preLoad = preLoad;
        this.mapperRegistryKey = mapperRegistryKey;
        this.properties = properties;
        this.version = version;
    }

    public T getProperties() {
        return properties;
    }

    public Boolean isPreLoad() {
        return preLoad;
    }

    public String getMapperRegistryKey() {
        return mapperRegistryKey;
    }

    public ServiceDiscoveryWrapper buildServiceDiscovery(IRegistryConnectionRepository discoveryConnectionRepository) throws Exception {
        if (properties instanceof DiscoveryProperties) {
            if (StringUtils.isEmpty(mapperRegistryKey))
                return null;
            ConnectionWrapper connectionWrapper = discoveryConnectionRepository.connection(mapperRegistryKey);
            if (connectionWrapper == null) {
                discoveryConnectionRepository.connectionLoad(mapperRegistryKey);
                connectionWrapper = discoveryConnectionRepository.connection(mapperRegistryKey);
                if (connectionWrapper == null)
                    return null;
            }
            CuratorFramework curatorFramework = connectionWrapper.getConnection(CuratorFramework.class);
            DiscoveryProperties discoveryProperties = (DiscoveryProperties) properties;
            DiscoveryBuilder discoveryBuilder = new DiscoveryBuilder(curatorFramework, discoveryProperties.getZkRoot());
            ServiceDiscovery<ZookeeperInstance> serviceDiscovery = discoveryBuilder.build();
            serviceDiscovery.start();
            return new ServiceDiscoveryWrapper(serviceDiscovery, dscrId, mapperRegistryKey, version);
        }
        return null;
    }

    public String getDscrId() {
        return dscrId;
    }

}
