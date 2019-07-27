package com.doctorwork.sword.gateway.admin.core.dto;

import com.doctorwork.doctorwork.admin.api.req.PayloadPing;
import com.doctorwork.sword.gateway.common.Serialize;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;

/**
 * @author chenzhiqiang
 * @date 2019/7/27
 */
public class PingParam implements Serialize {
    private String pingMode;
    private Integer pingIntervalTime = 20;
    private Integer maxTotalPingTime = 2;
    private String pingStrategy = "serial";
    private String uri;
    private Boolean secure;

    private PingParam() {
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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Boolean getSecure() {
        return secure;
    }

    public void setSecure(Boolean secure) {
        this.secure = secure;
    }

    public static PingParam param(PayloadPing payloadPing) {
        if (payloadPing == null || StringUtils.isEmpty(payloadPing.getPingMode()))
            return null;
        PingParam pingParam = new PingParam();
        pingParam.setPingMode(payloadPing.getPingMode());
        pingParam.setMaxTotalPingTime(payloadPing.getMaxTotalPingTime());
        pingParam.setPingIntervalTime(payloadPing.getPingIntervalTime());
        pingParam.setPingStrategy(payloadPing.getPingStrategy());
        pingParam.setUri(payloadPing.getPingUrl());
        return pingParam;
    }
}
