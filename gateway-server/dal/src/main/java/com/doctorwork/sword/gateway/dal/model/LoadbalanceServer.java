package com.doctorwork.sword.gateway.dal.model;

import java.io.Serializable;
import java.util.Date;

/**
 * This class corresponds to the database table <tt>loadbalance_server</tt>
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
public class LoadbalanceServer implements Serializable {
    /**
     * <pre>
     * 
     * This field corresponds to the database column <tt>loadbalance_server.id<tt>
     * </pre>
     */
    private Long id;

    /**
     * <pre>
     * 负载均衡标识
     * This field corresponds to the database column <tt>loadbalance_server.lb_mark<tt>
     * </pre>
     */
    private String lbMark;

    /**
     * <pre>
     * 服务id
     * This field corresponds to the database column <tt>loadbalance_server.srv_id<tt>
     * </pre>
     */
    private Long srvId;

    /**
     * <pre>
     * 服务IP
     * This field corresponds to the database column <tt>loadbalance_server.srv_ip<tt>
     * </pre>
     */
    private String srvIp;

    /**
     * <pre>
     * 服务端口
     * This field corresponds to the database column <tt>loadbalance_server.srv_port<tt>
     * </pre>
     */
    private Integer srvPort;

    /**
     * <pre>
     * 服务名称
     * This field corresponds to the database column <tt>loadbalance_server.srv_name<tt>
     * </pre>
     */
    private String srvName;

    /**
     * <pre>
     * 服务权重
     * This field corresponds to the database column <tt>loadbalance_server.srv_weight<tt>
     * </pre>
     */
    private Integer srvWeight;

    /**
     * <pre>
     * apollo对应的appId
     * This field corresponds to the database column <tt>loadbalance_server.apollo_id<tt>
     * </pre>
     */
    private String apolloId;

    /**
     * <pre>
     * 备注
     * This field corresponds to the database column <tt>loadbalance_server.comment<tt>
     * </pre>
     */
    private String comment;

    /**
     * <pre>
     * 是否删除，0-未删除，1-删除，默认为0
     * This field corresponds to the database column <tt>loadbalance_server.is_delete<tt>
     * </pre>
     */
    private Integer isDelete;

    /**
     * <pre>
     * Create time, common column by DB rules
     * This field corresponds to the database column <tt>loadbalance_server.gmt_create<tt>
     * </pre>
     */
    private Date gmtCreate;

    /**
     * <pre>
     * Modified time,common column by DB rules 
     * This field corresponds to the database column <tt>loadbalance_server.gmt_modified<tt>
     * </pre>
     */
    private Date gmtModified;

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.id<tt>
     * </pre>
     *
     * @return 
     */
    public Long getId() {
        return id;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.id<tt>
     * </pre>
     *
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.lb_mark<tt>
     * </pre>
     *
     * @return 负载均衡标识
     */
    public String getLbMark() {
        return lbMark;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.lb_mark<tt>
     * </pre>
     *
     * @param lbMark 负载均衡标识
     */
    public void setLbMark(String lbMark) {
        this.lbMark = lbMark == null ? null : lbMark.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.srv_id<tt>
     * </pre>
     *
     * @return 服务id
     */
    public Long getSrvId() {
        return srvId;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.srv_id<tt>
     * </pre>
     *
     * @param srvId 服务id
     */
    public void setSrvId(Long srvId) {
        this.srvId = srvId;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.srv_ip<tt>
     * </pre>
     *
     * @return 服务IP
     */
    public String getSrvIp() {
        return srvIp;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.srv_ip<tt>
     * </pre>
     *
     * @param srvIp 服务IP
     */
    public void setSrvIp(String srvIp) {
        this.srvIp = srvIp == null ? null : srvIp.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.srv_port<tt>
     * </pre>
     *
     * @return 服务端口
     */
    public Integer getSrvPort() {
        return srvPort;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.srv_port<tt>
     * </pre>
     *
     * @param srvPort 服务端口
     */
    public void setSrvPort(Integer srvPort) {
        this.srvPort = srvPort;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.srv_name<tt>
     * </pre>
     *
     * @return 服务名称
     */
    public String getSrvName() {
        return srvName;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.srv_name<tt>
     * </pre>
     *
     * @param srvName 服务名称
     */
    public void setSrvName(String srvName) {
        this.srvName = srvName == null ? null : srvName.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.srv_weight<tt>
     * </pre>
     *
     * @return 服务权重
     */
    public Integer getSrvWeight() {
        return srvWeight;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.srv_weight<tt>
     * </pre>
     *
     * @param srvWeight 服务权重
     */
    public void setSrvWeight(Integer srvWeight) {
        this.srvWeight = srvWeight;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.apollo_id<tt>
     * </pre>
     *
     * @return apollo对应的appId
     */
    public String getApolloId() {
        return apolloId;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.apollo_id<tt>
     * </pre>
     *
     * @param apolloId apollo对应的appId
     */
    public void setApolloId(String apolloId) {
        this.apolloId = apolloId == null ? null : apolloId.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.comment<tt>
     * </pre>
     *
     * @return 备注
     */
    public String getComment() {
        return comment;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.comment<tt>
     * </pre>
     *
     * @param comment 备注
     */
    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.is_delete<tt>
     * </pre>
     *
     * @return 是否删除，0-未删除，1-删除，默认为0
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.is_delete<tt>
     * </pre>
     *
     * @param isDelete 是否删除，0-未删除，1-删除，默认为0
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.gmt_create<tt>
     * </pre>
     *
     * @return Create time, common column by DB rules
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.gmt_create<tt>
     * </pre>
     *
     * @param gmtCreate Create time, common column by DB rules
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.gmt_modified<tt>
     * </pre>
     *
     * @return Modified time,common column by DB rules 
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_server.gmt_modified<tt>
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
        sb.append(", srvId=").append(srvId);
        sb.append(", srvIp=").append(srvIp);
        sb.append(", srvPort=").append(srvPort);
        sb.append(", srvName=").append(srvName);
        sb.append(", srvWeight=").append(srvWeight);
        sb.append(", apolloId=").append(apolloId);
        sb.append(", comment=").append(comment);
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
        LoadbalanceServer other = (LoadbalanceServer) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLbMark() == null ? other.getLbMark() == null : this.getLbMark().equals(other.getLbMark()))
            && (this.getSrvId() == null ? other.getSrvId() == null : this.getSrvId().equals(other.getSrvId()))
            && (this.getSrvIp() == null ? other.getSrvIp() == null : this.getSrvIp().equals(other.getSrvIp()))
            && (this.getSrvPort() == null ? other.getSrvPort() == null : this.getSrvPort().equals(other.getSrvPort()))
            && (this.getSrvName() == null ? other.getSrvName() == null : this.getSrvName().equals(other.getSrvName()))
            && (this.getSrvWeight() == null ? other.getSrvWeight() == null : this.getSrvWeight().equals(other.getSrvWeight()))
            && (this.getApolloId() == null ? other.getApolloId() == null : this.getApolloId().equals(other.getApolloId()))
            && (this.getComment() == null ? other.getComment() == null : this.getComment().equals(other.getComment()))
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
        result = prime * result + ((getSrvId() == null) ? 0 : getSrvId().hashCode());
        result = prime * result + ((getSrvIp() == null) ? 0 : getSrvIp().hashCode());
        result = prime * result + ((getSrvPort() == null) ? 0 : getSrvPort().hashCode());
        result = prime * result + ((getSrvName() == null) ? 0 : getSrvName().hashCode());
        result = prime * result + ((getSrvWeight() == null) ? 0 : getSrvWeight().hashCode());
        result = prime * result + ((getApolloId() == null) ? 0 : getApolloId().hashCode());
        result = prime * result + ((getComment() == null) ? 0 : getComment().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        return result;
    }
}