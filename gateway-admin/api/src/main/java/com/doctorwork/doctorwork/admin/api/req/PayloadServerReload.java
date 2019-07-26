package com.doctorwork.doctorwork.admin.api.req;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:10 2019/7/26
 * @Modified By:
 */
public class PayloadServerReload {
    private Boolean autoRefresh;
    private Integer payloadRefreshInitialDelayMs;
    private Integer payloadRefreshIntervalMs;

    public Integer getPayloadRefreshInitialDelayMs() {
        return payloadRefreshInitialDelayMs;
    }

    public void setPayloadRefreshInitialDelayMs(Integer payloadRefreshInitialDelayMs) {
        this.payloadRefreshInitialDelayMs = payloadRefreshInitialDelayMs;
    }

    public Integer getPayloadRefreshIntervalMs() {
        return payloadRefreshIntervalMs;
    }

    public void setPayloadRefreshIntervalMs(Integer payloadRefreshIntervalMs) {
        this.payloadRefreshIntervalMs = payloadRefreshIntervalMs;
    }

    public Boolean getAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(Boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }
}
