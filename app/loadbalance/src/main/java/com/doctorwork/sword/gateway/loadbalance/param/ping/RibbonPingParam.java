package com.doctorwork.sword.gateway.loadbalance.param.ping;

import com.doctorwork.sword.gateway.common.Constants;
import com.doctorwork.sword.gateway.loadbalance.param.PingParam;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.netflix.loadbalancer.IPing;
import org.springframework.util.StringUtils;

/**
 * @author chenzhiqiang
 * @date 2019/6/22
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "pingMode", include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class RibbonPingParam<T extends IPing> extends PingParam<T> {
    private String pingMode;
    private Integer pingIntervalTime = 20;
    private Integer maxTotalPingTime = 2;
    private String pingStrategy = "serial";

    public RibbonPingParam(String pingMode, String pingStrategy) {
        super("ribbon");
        if (StringUtils.isEmpty(pingMode))
            this.pingMode = Constants.PINGMODE_DUMMY;
        else
            this.pingMode = pingMode;
        if (StringUtils.isEmpty(pingStrategy)) {
            this.pingStrategy = Constants.PINGSTRATEGY_SERIAL;
        } else {
            this.pingStrategy = pingStrategy;
        }
    }

    public RibbonPingParam(String pingMode, Integer pingIntervalTime, Integer maxTotalPingTime, String pingStrategy) {
        this(pingMode, pingStrategy);
        this.pingIntervalTime = pingIntervalTime;
        this.maxTotalPingTime = maxTotalPingTime;
    }

    public String getPingMode() {
        return pingMode;
    }

    public void setPingMode(String pingMode) {
        this.pingMode = pingMode;
    }

    public Integer getPingIntervalTime() {
        return pingIntervalTime;
    }

    public void setPingIntervalTime(Integer pingIntervalTime) {
        this.pingIntervalTime = pingIntervalTime;
    }

    public Integer getMaxTotalPingTime() {
        return maxTotalPingTime;
    }

    public void setMaxTotalPingTime(Integer maxTotalPingTime) {
        this.maxTotalPingTime = maxTotalPingTime;
    }

    public String getPingStrategy() {
        return pingStrategy;
    }

    public void setPingStrategy(String pingStrategy) {
        this.pingStrategy = pingStrategy;
    }
}
