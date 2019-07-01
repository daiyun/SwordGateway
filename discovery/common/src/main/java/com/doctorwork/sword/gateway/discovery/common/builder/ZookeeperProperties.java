package com.doctorwork.sword.gateway.discovery.common.builder;

import com.doctorwork.sword.gateway.discovery.common.Constants;

import java.util.concurrent.TimeUnit;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:57 2019/6/3
 * @Modified By:
 */
public class ZookeeperProperties {
    private String connectString = Constants.DEFAULT_ZK_IPADDR;
    private Integer baseSleepTimeMs = 50;
    private Integer maxRetries = 10;
    private Integer maxSleepMs = 500;
    private Integer blockUntilConnectedWait = 10;
    private TimeUnit blockUntilConnectedUnit;

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public Integer getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setBaseSleepTimeMs(Integer baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(Integer maxRetries) {
        this.maxRetries = maxRetries;
    }

    public Integer getMaxSleepMs() {
        return maxSleepMs;
    }

    public void setMaxSleepMs(Integer maxSleepMs) {
        this.maxSleepMs = maxSleepMs;
    }

    public Integer getBlockUntilConnectedWait() {
        return blockUntilConnectedWait;
    }

    public void setBlockUntilConnectedWait(Integer blockUntilConnectedWait) {
        this.blockUntilConnectedWait = blockUntilConnectedWait;
    }

    public TimeUnit getBlockUntilConnectedUnit() {
        return blockUntilConnectedUnit;
    }

    public void setBlockUntilConnectedUnit(TimeUnit blockUntilConnectedUnit) {
        this.blockUntilConnectedUnit = blockUntilConnectedUnit;
    }
}
