package com.doctorwork.doctorwork.admin.api.res;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:43 2019/7/23
 * @Modified By:
 */
public class RoutePredicateRes {
    /**
     * <pre>
     * 路由谓词信息
     * This field corresponds to the database column <tt>route_predicate.route_predicate_key<tt>
     * </pre>
     */
    private String routePredicateKey;

    /**
     * <pre>
     * 路由谓词信息
     * This field corresponds to the database column <tt>route_predicate.route_predicate_value<tt>
     * </pre>
     */
    private String routePredicateValue;

    /**
     * <pre>
     * 路由谓词规则说明
     * This field corresponds to the database column <tt>route_predicate.route_predicate_comment<tt>
     * </pre>
     */
    private String routePredicateComment;

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

    public String getRoutePredicateComment() {
        return routePredicateComment;
    }

    public void setRoutePredicateComment(String routePredicateComment) {
        this.routePredicateComment = routePredicateComment;
    }
}
