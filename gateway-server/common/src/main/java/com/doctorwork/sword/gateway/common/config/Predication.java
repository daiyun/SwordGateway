package com.doctorwork.sword.gateway.common.config;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:03 2019/7/22
 * @Modified By:
 */
public class Predication {
    /**
     * 路由谓词信息
     */
    private String routePredicateKey;

    /**
     * 路由谓词信息
     */
    private String routePredicateValue;

    public String getRoutePredicateKey() {
        return routePredicateKey;
    }

    public void setRoutePredicateKey(String routePredicateKey) {
        this.routePredicateKey = routePredicateKey;
    }

    public String getRoutePredicateValue() {
        return routePredicateValue;
    }

    public void setRoutePredicateValue(String routePredicateValue) {
        this.routePredicateValue = routePredicateValue;
    }
}
