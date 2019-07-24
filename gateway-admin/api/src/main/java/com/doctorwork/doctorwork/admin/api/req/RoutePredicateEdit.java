package com.doctorwork.doctorwork.admin.api.req;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:48 2019/7/24
 * @Modified By:
 */
public class RoutePredicateEdit {
    private String routeMark;
    private String path;
    private String routePredicateValue;
    private String routePredicateComment;

    public String getRouteMark() {
        return routeMark;
    }

    public void setRouteMark(String routeMark) {
        this.routeMark = routeMark;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRoutePredicateValue() {
        return routePredicateValue;
    }

    public void setRoutePredicateValue(String routePredicateValue) {
        this.routePredicateValue = routePredicateValue;
    }

    public String getRoutePredicateComment() {
        return routePredicateComment;
    }

    public void setRoutePredicateComment(String routePredicateComment) {
        this.routePredicateComment = routePredicateComment;
    }
}
