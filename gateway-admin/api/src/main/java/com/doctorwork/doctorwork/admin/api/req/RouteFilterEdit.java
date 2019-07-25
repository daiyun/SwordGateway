package com.doctorwork.doctorwork.admin.api.req;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:48 2019/7/24
 * @Modified By:
 */
public class RouteFilterEdit {
    private String id;
    private String routeMark;
    private String routeFilterKey;
    private String routeFilterValue;
    private String routeFilterComment;

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

    public String getRouteMark() {
        return routeMark;
    }

    public void setRouteMark(String routeMark) {
        this.routeMark = routeMark;
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
}
