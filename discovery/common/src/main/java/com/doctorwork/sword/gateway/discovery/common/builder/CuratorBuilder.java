package com.doctorwork.sword.gateway.discovery.common.builder;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:08 2019/6/3
 * @Modified By:
 */
public class CuratorBuilder extends AbstractBuild<CuratorFramework> {

    private final ZookeeperProperties zookeeperProperties;

    public CuratorBuilder(ZookeeperProperties zookeeperProperties) {
        this.zookeeperProperties = zookeeperProperties;
    }

    public CuratorFramework build() {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        builder.connectString(zookeeperProperties.getConnectString());
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(zookeeperProperties.getBaseSleepTimeMs(),
                zookeeperProperties.getMaxRetries(), zookeeperProperties.getMaxSleepMs());
        CuratorFramework curator = builder.retryPolicy(retryPolicy).build();
        curator.start();
        try {
            curator.blockUntilConnected(zookeeperProperties.getBlockUntilConnectedWait(), zookeeperProperties.getBlockUntilConnectedUnit());
        } catch (InterruptedException e) {
            throw new RuntimeException("Block until a connection to ZooKeeper is available or the maxWaitTime has been exceeded");
        }
        return curator;
    }
}
