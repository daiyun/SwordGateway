package com.doctorwork.sword.gateway.discovery.common.builder;

import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:14 2019/6/3
 * @Modified By:
 */
public class DiscoveryBuilder extends AbstractBuild<ServiceDiscovery<ZookeeperInstance>> {
    private final CuratorFramework curatorFramework;
    private final String basePath;

    public DiscoveryBuilder(CuratorFramework curatorFramework, String basePath) {
        this.curatorFramework = curatorFramework;
        this.basePath = basePath;
    }

    public ServiceDiscovery<ZookeeperInstance> build() {
        return ServiceDiscoveryBuilder.builder(ZookeeperInstance.class)
                .client(this.curatorFramework)
                .basePath(this.basePath)
                .serializer(new JsonInstanceSerializer<>(ZookeeperInstance.class))
                .build();
    }
}
