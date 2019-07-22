package com.doctorwork.sword.gateway.common.config;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:05 2019/7/22
 * @Modified By:
 */
public class FilterInfo {
    /**
     * 路由过滤器信息
     */
    private String routeFilterKey;

    /**
     * 路由过滤器信息
     */
    private String routeFilterValue;

    public String getRouteFilterKey() {
        return routeFilterKey;
    }

    public void setRouteFilterKey(String routeFilterKey) {
        this.routeFilterKey = routeFilterKey;
    }

    public String getRouteFilterValue() {
        return routeFilterValue;
    }

    public void setRouteFilterValue(String routeFilterValue) {
        this.routeFilterValue = routeFilterValue;
    }
}
