package com.doctorwork.com.sword.gateway.registry.config;

import com.doctorwork.com.sword.gateway.registry.wrapper.ConnectionWrapper;
import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.common.config.ConnectionInfo;
import com.doctorwork.sword.gateway.discovery.common.builder.CuratorBuilder;
import com.doctorwork.sword.gateway.discovery.common.builder.ZookeeperProperties;
import org.apache.curator.framework.CuratorFramework;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:54 2019/7/4
 * @Modified By:
 */
public class RegistryConfig<T> {
    private String registryKey;
    private T properties;

    public static RegistryConfig build(ConnectionInfo connectionInfo) {
        ZookeeperProperties zookeeperProperties = JacksonUtil.toObject(connectionInfo.getConfig(), ZookeeperProperties.class);
        RegistryConfig<ZookeeperProperties> registryConfig = new RegistryConfig<>();
        registryConfig.setRegistryKey(connectionInfo.getId());
        registryConfig.setProperties(zookeeperProperties);
        return registryConfig;
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

    public ConnectionWrapper buidRegistry() {
        if (properties instanceof ZookeeperProperties) {
            ZookeeperProperties castProperties = (ZookeeperProperties) properties;
            CuratorBuilder curatorBuilder = new CuratorBuilder(castProperties);
            CuratorFramework curatorFramework = curatorBuilder.build();
            ConnectionWrapper connectionWrapper = new ConnectionWrapper(registryKey, curatorFramework);
            return connectionWrapper;
        }
        return null;
    }
}
