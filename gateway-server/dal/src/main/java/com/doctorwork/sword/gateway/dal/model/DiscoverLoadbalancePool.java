package com.doctorwork.sword.gateway.dal.model;

import java.io.Serializable;
import java.util.Date;

/**
 * This class corresponds to the database table <tt>discover_loadbalance_pool</tt>
 * 
 * This file is generated by <tt>dwframe<tt>
 * 
 * PLEASE DO NOT MODIFY THIS FILE MANUALLY, or else your modification may be
 * OVERWRITTEN by someone else. To modify the file, you should go to find the file
 * <tt>{project-home}/dalgen/mybatis_generator.xml<tt>. Modify the configuration file
 * according to your needs, then run <tt>ant</tt> to generate this file in {project-home}/dalgen.
 * 
 * @author dwframe
 * @since 2019-07-02
 */
public class DiscoverLoadbalancePool implements Serializable {
    /**
     * <pre>
     * 
     * This field corresponds to the database column <tt>discover_loadbalance_pool.id<tt>
     * </pre>
     */
    private Long id;

    /**
     * <pre>
     * 负载均衡标识
     * This field corresponds to the database column <tt>discover_loadbalance_pool.lb_mark<tt>
     * </pre>
     */
    private String lbMark;

    /**
     * <pre>
     * 服务发现配置项
     * This field corresponds to the database column <tt>discover_loadbalance_pool.dscr_id<tt>
     * </pre>
     */
    private String dscrId;

    /**
     * <pre>
     * 是否删除，0-未删除，1-删除，默认为0
     * This field corresponds to the database column <tt>discover_loadbalance_pool.is_delete<tt>
     * </pre>
     */
    private Integer isDelete;

    /**
     * <pre>
     * Create time, common column by DB rules
     * This field corresponds to the database column <tt>discover_loadbalance_pool.gmt_create<tt>
     * </pre>
     */
    private Date gmtCreate;

    /**
     * <pre>
     * Modified time,common column by DB rules 
     * This field corresponds to the database column <tt>discover_loadbalance_pool.gmt_modified<tt>
     * </pre>
     */
    private Date gmtModified;

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * <pre>
     * This field corresponds to the database column <tt>discover_loadbalance_pool.id<tt>
     * </pre>
     *
     * @return 
     */
    public Long getId() {
        return id;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>discover_loadbalance_pool.id<tt>
     * </pre>
     *
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>discover_loadbalance_pool.lb_mark<tt>
     * </pre>
     *
     * @return 负载均衡标识
     */
    public String getLbMark() {
        return lbMark;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>discover_loadbalance_pool.lb_mark<tt>
     * </pre>
     *
     * @param lbMark 负载均衡标识
     */
    public void setLbMark(String lbMark) {
        this.lbMark = lbMark == null ? null : lbMark.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>discover_loadbalance_pool.dscr_id<tt>
     * </pre>
     *
     * @return 服务发现配置项
     */
    public String getDscrId() {
        return dscrId;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>discover_loadbalance_pool.dscr_id<tt>
     * </pre>
     *
     * @param dscrId 服务发现配置项
     */
    public void setDscrId(String dscrId) {
        this.dscrId = dscrId == null ? null : dscrId.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>discover_loadbalance_pool.is_delete<tt>
     * </pre>
     *
     * @return 是否删除，0-未删除，1-删除，默认为0
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>discover_loadbalance_pool.is_delete<tt>
     * </pre>
     *
     * @param isDelete 是否删除，0-未删除，1-删除，默认为0
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>discover_loadbalance_pool.gmt_create<tt>
     * </pre>
     *
     * @return Create time, common column by DB rules
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>discover_loadbalance_pool.gmt_create<tt>
     * </pre>
     *
     * @param gmtCreate Create time, common column by DB rules
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>discover_loadbalance_pool.gmt_modified<tt>
     * </pre>
     *
     * @return Modified time,common column by DB rules 
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>discover_loadbalance_pool.gmt_modified<tt>
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
        sb.append(", lbMark=").append(lbMark);
        sb.append(", dscrId=").append(dscrId);
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
        DiscoverLoadbalancePool other = (DiscoverLoadbalancePool) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLbMark() == null ? other.getLbMark() == null : this.getLbMark().equals(other.getLbMark()))
            && (this.getDscrId() == null ? other.getDscrId() == null : this.getDscrId().equals(other.getDscrId()))
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
        result = prime * result + ((getLbMark() == null) ? 0 : getLbMark().hashCode());
        result = prime * result + ((getDscrId() == null) ? 0 : getDscrId().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        return result;
    }
}