package com.doctorwork.doctorwork.admin.api.req;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:00 2019/7/26
 * @Modified By:
 */
public class PayloadPing {
    private String pingMode;
    private Integer pingIntervalTime;
    private Integer maxTotalPingTime;
    private String pingStrategy;

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
