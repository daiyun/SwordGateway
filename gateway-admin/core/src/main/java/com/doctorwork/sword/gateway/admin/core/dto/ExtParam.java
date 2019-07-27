package com.doctorwork.sword.gateway.admin.core.dto;

import com.doctorwork.doctorwork.admin.api.req.PayloadServerReload;
import com.doctorwork.sword.gateway.common.Serialize;

/**
 * @author chenzhiqiang
 * @date 2019/7/27
 */
public class ExtParam implements Serialize {
    //服务自动刷新标识
    private Boolean autoRefresh;
    //服务刷新初始延迟时间 （默认1秒）
    private Long initialDelayMs;
    //服务刷新延迟时间间隔 （默认30秒）
    private Long refreshIntervalMs;

    private ExtParam() {
    }

    public Boolean getAutoRefresh() {
        return autoRefresh;
    }

    public void setAutoRefresh(Boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    public Long getInitialDelayMs() {
        return initialDelayMs;
    }

    public void setInitialDelayMs(Long initialDelayMs) {
        this.initialDelayMs = initialDelayMs;
    }

    public Long getRefreshIntervalMs() {
        return refreshIntervalMs;
    }

    public void setRefreshIntervalMs(Long refreshIntervalMs) {
        this.refreshIntervalMs = refreshIntervalMs;
    }

    public static ExtParam param(PayloadServerReload reload) {
        if (reload == null)
            return null;
        ExtParam extParam = new ExtParam();
        extParam.setAutoRefresh(reload.getAutoRefresh());
        extParam.setInitialDelayMs(Long.valueOf(reload.getPayloadRefreshInitialDelayMs()));
        extParam.setRefreshIntervalMs(Long.valueOf(reload.getPayloadRefreshIntervalMs()));
        return extParam;
    }
}
