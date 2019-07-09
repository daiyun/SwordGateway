package com.doctorwork.sword.gateway.discovery.config;

import com.doctorwork.com.sword.gateway.registry.IRegistryConnectionRepository;
import com.doctorwork.com.sword.gateway.registry.wrapper.DiscoveryConnectionWrapper;
import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.discovery.common.DiscoveryProperties;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import com.doctorwork.sword.gateway.discovery.common.builder.DiscoveryBuilder;
import com.doctorwork.sword.gateway.discovery.common.builder.ZookeeperProperties;
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

    public static DiscoveryConfig build(DiscoverConfig discoverConfig) {
        DiscoveryProperties discoveryProperties = JacksonUtil.toObject(discoverConfig.getDscrConfig(), DiscoveryProperties.class);
        DiscoveryConfig<DiscoveryProperties> discoveryConfig = new DiscoveryConfig<>(discoverConfig.getDscrId(),
                discoverConfig.getDscrPreloadEnable() == 0, discoverConfig.getDscrRegitryId(), discoveryProperties);
        return discoveryConfig;
    }

    public DiscoveryConfig(String dscrId, Boolean preLoad, String mapperRegistryKey, T properties) {
        this.dscrId = dscrId;
        this.preLoad = preLoad;
        this.mapperRegistryKey = mapperRegistryKey;
        this.properties = properties;
    }

    public T getProperties() {
        return properties;
    }

    public static void main(String[] args) {
        System.out.println(JacksonUtil.toJSon(new DiscoveryProperties()));
        System.out.println(JacksonUtil.toJSon(new ZookeeperProperties()));
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
            DiscoveryConnectionWrapper connectionWrapper = discoveryConnectionRepository.connection(mapperRegistryKey);
            if (connectionWrapper == null)
                return null;
            CuratorFramework curatorFramework = (CuratorFramework) connectionWrapper.getConnection();
            DiscoveryProperties discoveryProperties = (DiscoveryProperties) properties;
            DiscoveryBuilder discoveryBuilder = new DiscoveryBuilder(curatorFramework, discoveryProperties.getZkRoot());
            ServiceDiscovery<ZookeeperInstance> serviceDiscovery = discoveryBuilder.build();
            serviceDiscovery.start();
            return new ServiceDiscoveryWrapper(serviceDiscovery, dscrId, mapperRegistryKey);
        }
        return null;
    }

    public String getDscrId() {
        return dscrId;
    }

}
