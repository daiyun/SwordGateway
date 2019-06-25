package com.doctorwork.sword.gateway.loadbalance.param.ext;

import com.doctorwork.sword.gateway.common.Constants;
import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author chenzhiqiang
 * @date 2019/6/22
 */
@JsonTypeName(Constants.LBTYPE_RIBBON)
public class RibbonLoadBalanceParam extends LoadbalanceParam {
    //服务自动刷新标识
    private Boolean autoRefresh;
    //服务刷新初始延迟时间 （默认1秒）
    private Long initialDelayMs;
    //服务刷新延迟时间间隔 （默认30秒）
    private Long refreshIntervalMs;

    public RibbonLoadBalanceParam() {
        super(Constants.LBTYPE_RIBBON);
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

    public static void main(String[] args) {
        System.out.println(JacksonUtil.toJSon(new RibbonLoadBalanceParam()));
    }
}
