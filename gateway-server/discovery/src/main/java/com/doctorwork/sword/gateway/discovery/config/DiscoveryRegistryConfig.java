package com.doctorwork.sword.gateway.discovery.config;

import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.dal.model.DiscoverRegistryConfig;
import com.doctorwork.sword.gateway.discovery.common.builder.CuratorBuilder;
import com.doctorwork.sword.gateway.discovery.common.builder.ZookeeperProperties;
import com.doctorwork.sword.gateway.discovery.connection.DiscoveryConnectionWrapper;
import org.apache.curator.framework.CuratorFramework;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:54 2019/7/4
 * @Modified By:
 */
public class DiscoveryRegistryConfig<T> {
    private String registryKey;
    private T properties;

    public static DiscoveryRegistryConfig build(DiscoverRegistryConfig discoverRegistryConfig) {
        ZookeeperProperties zookeeperProperties = JacksonUtil.toObject(discoverRegistryConfig.getDscrRegistryConfig(), ZookeeperProperties.class);
        DiscoveryRegistryConfig<ZookeeperProperties> discoveryRegistryConfig = new DiscoveryRegistryConfig<>();
        discoveryRegistryConfig.setRegistryKey(discoverRegistryConfig.getDscrRegistryId());
        discoveryRegistryConfig.setProperties(zookeeperProperties);
        return discoveryRegistryConfig;
    }

    public String getRegistryKey() {
        return registryKey;
    }

    public void setRegistryKey(String registryKey) {
        this.registryKey = registryKey;
    }

    public T getProperties() {
        return properties;
    }

    public void setProperties(T properties) {
        this.properties = properties;
    }

    public DiscoveryConnectionWrapper buidRegistry() {
        if (properties instanceof ZookeeperProperties) {
            ZookeeperProperties castProperties = (ZookeeperProperties) properties;
            CuratorBuilder curatorBuilder = new CuratorBuilder(castProperties);
            CuratorFramework curatorFramework = curatorBuilder.build();
            DiscoveryConnectionWrapper<CuratorFramework> connectionWrapper = new DiscoveryConnectionWrapper<>(registryKey, curatorFramework);
            return connectionWrapper;
        }
        return null;
    }
}
