package com.doctorwork.sword.gateway.dal.model;

import java.io.Serializable;
import java.util.Date;

/**
 * This class corresponds to the database table <tt>route_info</tt>
 * 
 * This file is generated by <tt>dwframe<tt>
 * 
 * PLEASE DO NOT MODIFY THIS FILE MANUALLY, or else your modification may be
 * OVERWRITTEN by someone else. To modify the file, you should go to find the file
 * <tt>{project-home}/dalgen/mybatis_generator.xml<tt>. Modify the configuration file
 * according to your needs, then run <tt>ant</tt> to generate this file in {project-home}/dalgen.
 * 
 * @author dwframe
 * @since 2019-07-01
 */
public class RouteInfo implements Serializable {
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

    /**
     * <pre>
     * 是否删除，0-未删除，1-删除，默认为0
     * This field corresponds to the database column <tt>route_info.is_delete<tt>
     * </pre>
     */
    private Integer isDelete;

    /**
     * <pre>
     * Create time, common column by DB rules
     * This field corresponds to the database column <tt>route_info.gmt_create<tt>
     * </pre>
     */
    private Date gmtCreate;

    /**
     * <pre>
     * Modified time,common column by DB rules 
     * This field corresponds to the database column <tt>route_info.gmt_modified<tt>
     * </pre>
     */
    private Date gmtModified;

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.id<tt>
     * </pre>
     *
     * @return 
     */
    public Long getId() {
        return id;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.id<tt>
     * </pre>
     *
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.route_mark<tt>
     * </pre>
     *
     * @return 路由标识
     */
    public String getRouteMark() {
        return routeMark;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.route_mark<tt>
     * </pre>
     *
     * @param routeMark 路由标识
     */
    public void setRouteMark(String routeMark) {
        this.routeMark = routeMark == null ? null : routeMark.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.route_name<tt>
     * </pre>
     *
     * @return 路由名称
     */
    public String getRouteName() {
        return routeName;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.route_name<tt>
     * </pre>
     *
     * @param routeName 路由名称
     */
    public void setRouteName(String routeName) {
        this.routeName = routeName == null ? null : routeName.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.route_target_mode<tt>
     * </pre>
     *
     * @return 路由转发目标方式:1.直联 2.负载均衡
     */
    public Integer getRouteTargetMode() {
        return routeTargetMode;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.route_target_mode<tt>
     * </pre>
     *
     * @param routeTargetMode 路由转发目标方式:1.直联 2.负载均衡
     */
    public void setRouteTargetMode(Integer routeTargetMode) {
        this.routeTargetMode = routeTargetMode;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.route_uri<tt>
     * </pre>
     *
     * @return 路由目标
     */
    public String getRouteUri() {
        return routeUri;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.route_uri<tt>
     * </pre>
     *
     * @param routeUri 路由目标
     */
    public void setRouteUri(String routeUri) {
        this.routeUri = routeUri == null ? null : routeUri.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.route_comment<tt>
     * </pre>
     *
     * @return 路由备注
     */
    public String getRouteComment() {
        return routeComment;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.route_comment<tt>
     * </pre>
     *
     * @param routeComment 路由备注
     */
    public void setRouteComment(String routeComment) {
        this.routeComment = routeComment == null ? null : routeComment.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.apollo_id<tt>
     * </pre>
     *
     * @return 应用的apollo对应的appId
     */
    public String getApolloId() {
        return apolloId;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.apollo_id<tt>
     * </pre>
     *
     * @param apolloId 应用的apollo对应的appId
     */
    public void setApolloId(String apolloId) {
        this.apolloId = apolloId == null ? null : apolloId.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.route_status<tt>
     * </pre>
     *
     * @return 路由状态：0 未启用 1 启用 2禁用
     */
    public Integer getRouteStatus() {
        return routeStatus;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.route_status<tt>
     * </pre>
     *
     * @param routeStatus 路由状态：0 未启用 1 启用 2禁用
     */
    public void setRouteStatus(Integer routeStatus) {
        this.routeStatus = routeStatus;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.route_sort<tt>
     * </pre>
     *
     * @return 路由排序
     */
    public Integer getRouteSort() {
        return routeSort;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.route_sort<tt>
     * </pre>
     *
     * @param routeSort 路由排序
     */
    public void setRouteSort(Integer routeSort) {
        this.routeSort = routeSort;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.is_delete<tt>
     * </pre>
     *
     * @return 是否删除，0-未删除，1-删除，默认为0
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.is_delete<tt>
     * </pre>
     *
     * @param isDelete 是否删除，0-未删除，1-删除，默认为0
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.gmt_create<tt>
     * </pre>
     *
     * @return Create time, common column by DB rules
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.gmt_create<tt>
     * </pre>
     *
     * @param gmtCreate Create time, common column by DB rules
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.gmt_modified<tt>
     * </pre>
     *
     * @return Modified time,common column by DB rules 
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_info.gmt_modified<tt>
     * </pre>
     *
     * @param gmtModified Modified time,common column by DB rules 
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    /**
     *
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", routeMark=").append(routeMark);
        sb.append(", routeName=").append(routeName);
        sb.append(", routeTargetMode=").append(routeTargetMode);
        sb.append(", routeUri=").append(routeUri);
        sb.append(", routeComment=").append(routeComment);
        sb.append(", apolloId=").append(apolloId);
        sb.append(", routeStatus=").append(routeStatus);
        sb.append(", routeSort=").append(routeSort);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append("]");
        return sb.toString();
    }

    /**
     *
     * @param that
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        RouteInfo other = (RouteInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRouteMark() == null ? other.getRouteMark() == null : this.getRouteMark().equals(other.getRouteMark()))
            && (this.getRouteName() == null ? other.getRouteName() == null : this.getRouteName().equals(other.getRouteName()))
            && (this.getRouteTargetMode() == null ? other.getRouteTargetMode() == null : this.getRouteTargetMode().equals(other.getRouteTargetMode()))
            && (this.getRouteUri() == null ? other.getRouteUri() == null : this.getRouteUri().equals(other.getRouteUri()))
            && (this.getRouteComment() == null ? other.getRouteComment() == null : this.getRouteComment().equals(other.getRouteComment()))
            && (this.getApolloId() == null ? other.getApolloId() == null : this.getApolloId().equals(other.getApolloId()))
            && (this.getRouteStatus() == null ? other.getRouteStatus() == null : this.getRouteStatus().equals(other.getRouteStatus()))
            && (this.getRouteSort() == null ? other.getRouteSort() == null : this.getRouteSort().equals(other.getRouteSort()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
            && (this.getGmtCreate() == null ? other.getGmtCreate() == null : this.getGmtCreate().equals(other.getGmtCreate()))
            && (this.getGmtModified() == null ? other.getGmtModified() == null : this.getGmtModified().equals(other.getGmtModified()));
    }

    /**
     *
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRouteMark() == null) ? 0 : getRouteMark().hashCode());
        result = prime * result + ((getRouteName() == null) ? 0 : getRouteName().hashCode());
        result = prime * result + ((getRouteTargetMode() == null) ? 0 : getRouteTargetMode().hashCode());
        result = prime * result + ((getRouteUri() == null) ? 0 : getRouteUri().hashCode());
        result = prime * result + ((getRouteComment() == null) ? 0 : getRouteComment().hashCode());
        result = prime * result + ((getApolloId() == null) ? 0 : getApolloId().hashCode());
        result = prime * result + ((getRouteStatus() == null) ? 0 : getRouteStatus().hashCode());
        result = prime * result + ((getRouteSort() == null) ? 0 : getRouteSort().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        return result;
    }
}