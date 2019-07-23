package com.doctorwork.doctorwork.admin.api.res;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:29 2019/7/23
 * @Modified By:
 */
public class RouteInfoRes {
    /**
     * <pre>
     *
     * This field corresponds to the database column <tt>route_info.id<tt>
     * </pre>
     */
    private Long id;

    /**
     * <pre>
     * 路由标识
     * This field corresponds to the database column <tt>route_info.route_mark<tt>
     * </pre>
     */
    private String routeMark;

    /**
     * <pre>
     * 路由名称
     * This field corresponds to the database column <tt>route_info.route_name<tt>
     * </pre>
     */
    private String routeName;

    /**
     * <pre>
     * 路由转发目标方式:1.直联 2.负载均衡
     * This field corresponds to the database column <tt>route_info.route_target_mode<tt>
     * </pre>
     */
    private Integer routeTargetMode;

    /**
     * <pre>
     * 路由目标
     * This field corresponds to the database column <tt>route_info.route_uri<tt>
     * </pre>
     */
    private String routeUri;

    /**
     * <pre>
     * 路由备注
     * This field corresponds to the database column <tt>route_info.route_comment<tt>
     * </pre>
     */
    private String routeComment;

    /**
     * <pre>
     * 应用的apollo对应的appId
     * This field corresponds to the database column <tt>route_info.apollo_id<tt>
     * </pre>
     */
    private String apolloId;

    /**
     * <pre>
     * 路由状态：0 未启用 1 启用 2禁用
     * This field corresponds to the database column <tt>route_info.route_status<tt>
     * </pre>
     */
    private Integer routeStatus;

    /**
     * <pre>
     * 路由排序
     * This field corresponds to the database column <tt>route_info.route_sort<tt>
     * </pre>
     */
    private Integer routeSort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getRouteTargetMode() {
        return routeTargetMode;
    }

    public void setRouteTargetMode(Integer routeTargetMode) {
        this.routeTargetMode = routeTargetMode;
    }

    public String getRouteUri() {
        return routeUri;
    }

    public void setRouteUri(String routeUri) {
        this.routeUri = routeUri;
    }

    public String getRouteComment() {
        return routeComment;
    }

    public void setRouteComment(String routeComment) {
        this.routeComment = routeComment;
    }

    public String getApolloId() {
        return apolloId;
    }

    public void setApolloId(String apolloId) {
        this.apolloId = apolloId;
    }

    public Integer getRouteStatus() {
        return routeStatus;
    }

    public void setRouteStatus(Integer routeStatus) {
        this.routeStatus = routeStatus;
    }

    public Integer getRouteSort() {
        return routeSort;
    }

    public void setRouteSort(Integer routeSort) {
        this.routeSort = routeSort;
    }
}
