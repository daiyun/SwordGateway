package com.doctorwork.sword.gateway.common.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:00 2019/7/22
 * @Modified By:
 */
public class RouteInfo {
    /**
     * 路由标识
     */
    private String routeMark;

    /**
     * 路由名称
     */
    private String routeName;

    /**
     * 路由目标
     */
    private String routeUri;

    /**
     * 路由排序
     */
    private Integer routeSort;

    private List<Predication> predications = new ArrayList();

    private List<FilterInfo> filters = new ArrayList();

    public String getRouteMark() {
        return routeMark;
    }

    public void setRouteMark(String routeMark) {
        this.routeMark = routeMark;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteUri() {
        return routeUri;
    }

    public void setRouteUri(String routeUri) {
        this.routeUri = routeUri;
    }

    public Integer getRouteSort() {
        return routeSort;
    }

    public void setRouteSort(Integer routeSort) {
        this.routeSort = routeSort;
    }

    public List<Predication> getPredications() {
        return predications;
    }

    public void setPredications(List<Predication> predications) {
        this.predications = predications;
    }

    public List<FilterInfo> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterInfo> filters) {
        this.filters = filters;
    }
}
