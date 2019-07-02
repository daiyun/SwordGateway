package com.doctorwork.sword.gateway.loadbalance.param;

import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.discovery.common.DiscoveryProperties;
import com.doctorwork.sword.gateway.discovery.common.builder.ZookeeperProperties;

/**
 * @Author:czq
 * @Description:
 * @Date: 14:39 2019/7/2
 * @Modified By:
 */
public class DiscoveryConfigParam implements Param {
    private ZookeeperProperties zookeeperProperties;
    private DiscoveryProperties discoveryProperties;

    public static DiscoveryConfigParam build(DiscoverConfig discoverConfig) {
        return JacksonUtil.toObject(discoverConfig.getDscrConfig(), DiscoveryConfigParam.class);
    }

    public ZookeeperProperties getZookeeperProperties() {
        return zookeeperProperties;
    }

    public void setZookeeperProperties(ZookeeperProperties zookeeperProperties) {
        this.zookeeperProperties = zookeeperProperties;
    }

    public DiscoveryProperties getDiscoveryProperties() {
        return discoveryProperties;
    }

    public void setDiscoveryProperties(DiscoveryProperties discoveryProperties) {
        this.discoveryProperties = discoveryProperties;
    }
}
