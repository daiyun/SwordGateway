package com.doctorwork.sword.gateway.dal.model;

import java.io.Serializable;
import java.util.Date;

/**
 * This class corresponds to the database table <tt>loadbalance_info</tt>
 * 
 * This file is generated by <tt>dwframe<tt>
 * 
 * PLEASE DO NOT MODIFY THIS FILE MANUALLY, or else your modification may be
 * OVERWRITTEN by someone else. To modify the file, you should go to find the file
 * <tt>{project-home}/dalgen/mybatis_generator.xml<tt>. Modify the configuration file
 * according to your needs, then run <tt>ant</tt> to generate this file in {project-home}/dalgen.
 * 
 * @author dwframe
 * @since 2019-07-28
 */
public class LoadbalanceInfo implements Serializable {
    /**
     * <pre>
     * 
     * This field corresponds to the database column <tt>loadbalance_info.id<tt>
     * </pre>
     */
    private Long id;

    /**
     * <pre>
     * 负载标识
     * This field corresponds to the database column <tt>loadbalance_info.lb_mark<tt>
     * </pre>
     */
    private String lbMark;

    /**
     * <pre>
     * 负载器名称
     * This field corresponds to the database column <tt>loadbalance_info.lb_name<tt>
     * </pre>
     */
    private String lbName;

    /**
     * <pre>
     * 负载器备注
     * This field corresponds to the database column <tt>loadbalance_info.lb_comment<tt>
     * </pre>
     */
    private String lbComment;

    /**
     * <pre>
     * 负载器类型
     * This field corresponds to the database column <tt>loadbalance_info.lb_type<tt>
     * </pre>
     */
    private String lbType;

    /**
     * <pre>
     * 负载规则参数
     * This field corresponds to the database column <tt>loadbalance_info.rule_param<tt>
     * </pre>
     */
    private String ruleParam;

    /**
     * <pre>
     * ping相关参数
     * This field corresponds to the database column <tt>loadbalance_info.ping_param<tt>
     * </pre>
     */
    private String pingParam;

    /**
     * <pre>
     * 负载器额外参数
     * This field corresponds to the database column <tt>loadbalance_info.lb_ext_param<tt>
     * </pre>
     */
    private String lbExtParam;

    /**
     * <pre>
     * 是否采用服务发现: 0默认不可用 1可用
     * This field corresponds to the database column <tt>loadbalance_info.dscr_enable<tt>
     * </pre>
     */
    private Integer dscrEnable;

    /**
     * <pre>
     * 服务发现标识
     * This field corresponds to the database column <tt>loadbalance_info.dscr_id<tt>
     * </pre>
     */
    private String dscrId;

    /**
     * <pre>
     * 是否启用，0默认未启用 1启用 2禁用
     * This field corresponds to the database column <tt>loadbalance_info.lb_status<tt>
     * </pre>
     */
    private Integer lbStatus;

    /**
     * <pre>
     * 是否删除，0-未删除，1-删除，默认为0
     * This field corresponds to the database column <tt>loadbalance_info.is_delete<tt>
     * </pre>
     */
    private Integer isDelete;

    /**
     * <pre>
     * Create time, common column by DB rules
     * This field corresponds to the database column <tt>loadbalance_info.gmt_create<tt>
     * </pre>
     */
    private Date gmtCreate;

    /**
     * <pre>
     * Modified time,common column by DB rules 
     * This field corresponds to the database column <tt>loadbalance_info.gmt_modified<tt>
     * </pre>
     */
    private Date gmtModified;

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.id<tt>
     * </pre>
     *
     * @return 
     */
    public Long getId() {
        return id;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.id<tt>
     * </pre>
     *
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.lb_mark<tt>
     * </pre>
     *
     * @return 负载标识
     */
    public String getLbMark() {
        return lbMark;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.lb_mark<tt>
     * </pre>
     *
     * @param lbMark 负载标识
     */
    public void setLbMark(String lbMark) {
        this.lbMark = lbMark == null ? null : lbMark.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.lb_name<tt>
     * </pre>
     *
     * @return 负载器名称
     */
    public String getLbName() {
        return lbName;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.lb_name<tt>
     * </pre>
     *
     * @param lbName 负载器名称
     */
    public void setLbName(String lbName) {
        this.lbName = lbName == null ? null : lbName.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.lb_comment<tt>
     * </pre>
     *
     * @return 负载器备注
     */
    public String getLbComment() {
        return lbComment;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.lb_comment<tt>
     * </pre>
     *
     * @param lbComment 负载器备注
     */
    public void setLbComment(String lbComment) {
        this.lbComment = lbComment == null ? null : lbComment.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.lb_type<tt>
     * </pre>
     *
     * @return 负载器类型
     */
    public String getLbType() {
        return lbType;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.lb_type<tt>
     * </pre>
     *
     * @param lbType 负载器类型
     */
    public void setLbType(String lbType) {
        this.lbType = lbType == null ? null : lbType.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.rule_param<tt>
     * </pre>
     *
     * @return 负载规则参数
     */
    public String getRuleParam() {
        return ruleParam;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.rule_param<tt>
     * </pre>
     *
     * @param ruleParam 负载规则参数
     */
    public void setRuleParam(String ruleParam) {
        this.ruleParam = ruleParam == null ? null : ruleParam.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.ping_param<tt>
     * </pre>
     *
     * @return ping相关参数
     */
    public String getPingParam() {
        return pingParam;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.ping_param<tt>
     * </pre>
     *
     * @param pingParam ping相关参数
     */
    public void setPingParam(String pingParam) {
        this.pingParam = pingParam == null ? null : pingParam.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.lb_ext_param<tt>
     * </pre>
     *
     * @return 负载器额外参数
     */
    public String getLbExtParam() {
        return lbExtParam;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.lb_ext_param<tt>
     * </pre>
     *
     * @param lbExtParam 负载器额外参数
     */
    public void setLbExtParam(String lbExtParam) {
        this.lbExtParam = lbExtParam == null ? null : lbExtParam.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.dscr_enable<tt>
     * </pre>
     *
     * @return 是否采用服务发现: 0默认不可用 1可用
     */
    public Integer getDscrEnable() {
        return dscrEnable;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.dscr_enable<tt>
     * </pre>
     *
     * @param dscrEnable 是否采用服务发现: 0默认不可用 1可用
     */
    public void setDscrEnable(Integer dscrEnable) {
        this.dscrEnable = dscrEnable;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.dscr_id<tt>
     * </pre>
     *
     * @return 服务发现标识
     */
    public String getDscrId() {
        return dscrId;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.dscr_id<tt>
     * </pre>
     *
     * @param dscrId 服务发现标识
     */
    public void setDscrId(String dscrId) {
        this.dscrId = dscrId == null ? null : dscrId.trim();
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.lb_status<tt>
     * </pre>
     *
     * @return 是否启用，0默认未启用 1启用 2禁用
     */
    public Integer getLbStatus() {
        return lbStatus;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.lb_status<tt>
     * </pre>
     *
     * @param lbStatus 是否启用，0默认未启用 1启用 2禁用
     */
    public void setLbStatus(Integer lbStatus) {
        this.lbStatus = lbStatus;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.is_delete<tt>
     * </pre>
     *
     * @return 是否删除，0-未删除，1-删除，默认为0
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.is_delete<tt>
     * </pre>
     *
     * @param isDelete 是否删除，0-未删除，1-删除，默认为0
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.gmt_create<tt>
     * </pre>
     *
     * @return Create time, common column by DB rules
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.gmt_create<tt>
     * </pre>
     *
     * @param gmtCreate Create time, common column by DB rules
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.gmt_modified<tt>
     * </pre>
     *
     * @return Modified time,common column by DB rules 
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * <pre>
     * This field corresponds to the database column <tt>loadbalance_info.gmt_modified<tt>
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
        sb.append(", lbName=").append(lbName);
        sb.append(", lbComment=").append(lbComment);
        sb.append(", lbType=").append(lbType);
        sb.append(", ruleParam=").append(ruleParam);
        sb.append(", pingParam=").append(pingParam);
        sb.append(", lbExtParam=").append(lbExtParam);
        sb.append(", dscrEnable=").append(dscrEnable);
        sb.append(", dscrId=").append(dscrId);
        sb.append(", lbStatus=").append(lbStatus);
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
        LoadbalanceInfo other = (LoadbalanceInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLbMark() == null ? other.getLbMark() == null : this.getLbMark().equals(other.getLbMark()))
            && (this.getLbName() == null ? other.getLbName() == null : this.getLbName().equals(other.getLbName()))
            && (this.getLbComment() == null ? other.getLbComment() == null : this.getLbComment().equals(other.getLbComment()))
            && (this.getLbType() == null ? other.getLbType() == null : this.getLbType().equals(other.getLbType()))
            && (this.getRuleParam() == null ? other.getRuleParam() == null : this.getRuleParam().equals(other.getRuleParam()))
            && (this.getPingParam() == null ? other.getPingParam() == null : this.getPingParam().equals(other.getPingParam()))
            && (this.getLbExtParam() == null ? other.getLbExtParam() == null : this.getLbExtParam().equals(other.getLbExtParam()))
            && (this.getDscrEnable() == null ? other.getDscrEnable() == null : this.getDscrEnable().equals(other.getDscrEnable()))
            && (this.getDscrId() == null ? other.getDscrId() == null : this.getDscrId().equals(other.getDscrId()))
            && (this.getLbStatus() == null ? other.getLbStatus() == null : this.getLbStatus().equals(other.getLbStatus()))
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
        result = prime * result + ((getLbName() == null) ? 0 : getLbName().hashCode());
        result = prime * result + ((getLbComment() == null) ? 0 : getLbComment().hashCode());
        result = prime * result + ((getLbType() == null) ? 0 : getLbType().hashCode());
        result = prime * result + ((getRuleParam() == null) ? 0 : getRuleParam().hashCode());
        result = prime * result + ((getPingParam() == null) ? 0 : getPingParam().hashCode());
        result = prime * result + ((getLbExtParam() == null) ? 0 : getLbExtParam().hashCode());
        result = prime * result + ((getDscrEnable() == null) ? 0 : getDscrEnable().hashCode());
        result = prime * result + ((getDscrId() == null) ? 0 : getDscrId().hashCode());
        result = prime * result + ((getLbStatus() == null) ? 0 : getLbStatus().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getGmtCreate() == null) ? 0 : getGmtCreate().hashCode());
        result = prime * result + ((getGmtModified() == null) ? 0 : getGmtModified().hashCode());
        return result;
    }
}