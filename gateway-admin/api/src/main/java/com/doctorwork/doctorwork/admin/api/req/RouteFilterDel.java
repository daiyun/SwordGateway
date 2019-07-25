package com.doctorwork.doctorwork.admin.api.req;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:48 2019/7/24
 * @Modified By:
 */
public class RouteFilterDel {
    private String id;
    private String routeFilterKey;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRouteFilterKey() {
        return routeFilterKey;
    }

    public void setRouteFilterKey(String routeFilterKey) {
        this.routeFilterKey = routeFilterKey;
    }
}
