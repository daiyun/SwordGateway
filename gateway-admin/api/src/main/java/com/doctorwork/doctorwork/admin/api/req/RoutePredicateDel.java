package com.doctorwork.doctorwork.admin.api.req;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:48 2019/7/24
 * @Modified By:
 */
public class RoutePredicateDel {
    private String id;
    private String routePredicateKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoutePredicateKey() {
        return routePredicateKey;
    }

    public void setRoutePredicateKey(String routePredicateKey) {
        this.routePredicateKey = routePredicateKey;
    }
}
