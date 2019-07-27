package com.doctorwork.sword.gateway.admin.dal.model;

import java.io.Serializable;
import java.util.Date;

/**
 * This class corresponds to the database table <tt>route_filter</tt>
 * 
 * This file is generated by <tt>dwframe<tt>
 * 
 * PLEASE DO NOT MODIFY THIS FILE MANUALLY, or else your modification may be
 * OVERWRITTEN by someone else. To modify the file, you should go to find the file
 * <tt>{project-home}/dalgen/mybatis_generator.xml<tt>. Modify the configuration file
 * according to your needs, then run <tt>ant</tt> to generate this file in {project-home}/dalgen.
 * 
 * @author dwframe
 * @since 2019-07-27
 */
public class RouteFilter implements Serializable {
    /**
     * <pre>
     * 
     * This field corresponds to the database column <tt>route_filter.id<tt>
     * </pre>
     */
    private Long id;

    /**
     * <pre>
     * 路由id
     * This field corresponds to the database column <tt>route_filter.route_id<tt>
     * </pre>
     */
    private Long routeId;

    /**
     * <pre>
     * 路由过滤器信息
     * This field corresponds to the database column <tt>route_filter.route_filter_key<tt>
     * </pre>
     */
    private String routeFilterKey;

    /**
     * <pre>
     * 路由过滤器信息
     * This field corresponds to the database column <tt>route_filter.route_filter_value<tt>
     * </pre>
     */
    private String routeFilterValue;

    /**
     * <pre>
     * 路由过滤器规则说明
     * This field corresponds to the database column <tt>route_filter.route_filter_comment<tt>
     * </pre>
     */
    private String routeFilterComment;

    /**
     * <pre>
     * 是否删除，0-未删除，1-删除，默认为0
     * This field corresponds to the database column <tt>route_filter.is_delete<tt>
     * </pre>
     */
    private Integer isDelete;

    /**
     * <pre>
     * Create time, common column by DB rules
     * This field corresponds to the database column <tt>route_filter.gmt_create<tt>
     * </pre>
     */
    private Date gmtCreate;

    /**
     * <pre>
     * Modified time,common column by DB rules 
     * This field corresponds to the database column <tt>route_filter.gmt_modified<tt>
     * </pre>
     */
    private Date gmtModified;

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.id<tt>
     * </pre>
     *
     * @return 
     */
    public Long getId() {
        return id;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.id<tt>
     * </pre>
     *
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.route_id<tt>
     * </pre>
     *
     * @return 路由id
     */
    public Long getRouteId() {
        return routeId;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.route_id<tt>
     * </pre>
     *
     * @param routeId 路由id
     */
    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.route_filter_key<tt>
     * </pre>
     *
     * @return 路由过滤器信息
     */
    public String getRouteFilterKey() {
        return routeFilterKey;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.route_filter_key<tt>
     * </pre>
     *
     * @param routeFilterKey 路由过滤器信息
     */
    public void setRouteFilterKey(String routeFilterKey) {
        this.routeFilterKey = routeFilterKey == null ? null : routeFilterKey.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.route_filter_value<tt>
     * </pre>
     *
     * @return 路由过滤器信息
     */
    public String getRouteFilterValue() {
        return routeFilterValue;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.route_filter_value<tt>
     * </pre>
     *
     * @param routeFilterValue 路由过滤器信息
     */
    public void setRouteFilterValue(String routeFilterValue) {
        this.routeFilterValue = routeFilterValue == null ? null : routeFilterValue.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.route_filter_comment<tt>
     * </pre>
     *
     * @return 路由过滤器规则说明
     */
    public String getRouteFilterComment() {
        return routeFilterComment;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.route_filter_comment<tt>
     * </pre>
     *
     * @param routeFilterComment 路由过滤器规则说明
     */
    public void setRouteFilterComment(String routeFilterComment) {
        this.routeFilterComment = routeFilterComment == null ? null : routeFilterComment.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.is_delete<tt>
     * </pre>
     *
     * @return 是否删除，0-未删除，1-删除，默认为0
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.is_delete<tt>
     * </pre>
     *
     * @param isDelete 是否删除，0-未删除，1-删除，默认为0
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.gmt_create<tt>
     * </pre>
     *
     * @return Create time, common column by DB rules
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.gmt_create<tt>
     * </pre>
     *
     * @param gmtCreate Create time, common column by DB rules
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.gmt_modified<tt>
     * </pre>
     *
     * @return Modified time,common column by DB rules 
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>route_filter.gmt_modified<tt>
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
        sb.append(", routeId=").append(routeId);
        sb.append(", routeFilterKey=").append(routeFilterKey);
        sb.append(", routeFilterValue=").append(routeFilterValue);
        sb.append(", routeFilterComment=").append(routeFilterComment);
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
        RouteFilter other = (RouteFilter) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRouteId() == null ? other.getRouteId() == null : this.getRouteId().equals(other.getRouteId()))
            && (this.getRouteFilterKey() == null ? other.getRouteFilterKey() == null : this.getRouteFilterKey().equals(other.getRouteFilterKey()))
            && (this.getRouteFilterValue() == null ? other.getRouteFilterValue() == null : this.getRouteFilterValue().equals(other.getRouteFilterValue()))
            && (this.getRouteFilterComment() == null ? other.getRouteFilterComment() == null : this.getRouteFilterComment().equals(other.getRouteFilterComment()))
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
        result = prime * result + ((getRouteId() == null) ? 0 : getRouteId().hashCode());
        result = prime * result + ((getRouteFilterKey() == null) ? 0 : getRouteFilterKey().hashCode());
        result = prime * result + ((getRouteFilterValue() == null) ? 0 : getRouteFilterValue().hashCode());
        result = prime * result + ((getRouteFilterComment() == null) ? 0 : getRouteFilterComment().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        return result;
    }
}