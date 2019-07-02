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
    private Boolean preLoad;
    private ZookeeperProperties zookeeperProperties;
    private DiscoveryProperties discoveryProperties;

    public static DiscoveryConfigParam build(DiscoverConfig discoverConfig) {
        DiscoveryConfigParam discoveryConfigParam = new DiscoveryConfigParam();
        discoveryConfigParam.setPreLoad(discoverConfig.getDscrPreloadEnable() == 0);
        ZookeeperProperties zookeeperProperties = JacksonUtil.toObject(discoverConfig.getDscrRegitryConfig(), ZookeeperProperties.class);
        DiscoveryProperties discoveryProperties = JacksonUtil.toObject(discoverConfig.getDscrConfig(), DiscoveryProperties.class);
        discoveryConfigParam.setZookeeperProperties(zookeeperProperties);
        discoveryConfigParam.setDiscoveryProperties(discoveryProperties);
        return discoveryConfigParam;
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

    public static void main(String[] args) {
        System.out.println(JacksonUtil.toJSon(new DiscoveryProperties()));
        System.out.println(JacksonUtil.toJSon(new ZookeeperProperties()));
    }

    public Boolean isPreLoad() {
        return preLoad;
    }

    public void setPreLoad(Boolean preLoad) {
        this.preLoad = preLoad;
    }
}
