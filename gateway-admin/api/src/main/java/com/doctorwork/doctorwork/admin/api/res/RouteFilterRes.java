package com.doctorwork.doctorwork.admin.api.res;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:43 2019/7/23
 * @Modified By:
 */
public class RouteFilterRes {

    private String id;

    private String routeFilterKey;

    private String routeFilterValue;

    private String routeFilterComment;

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

    public String getRouteFilterComment() {
        return routeFilterComment;
    }

    public void setRouteFilterComment(String routeFilterComment) {
        this.routeFilterComment = routeFilterComment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
