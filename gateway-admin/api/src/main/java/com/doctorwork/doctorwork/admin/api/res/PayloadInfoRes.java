package com.doctorwork.doctorwork.admin.api.res;

import com.doctorwork.doctorwork.admin.api.req.PayloadPing;
import com.doctorwork.doctorwork.admin.api.req.PayloadRule;
import com.doctorwork.doctorwork.admin.api.req.PayloadServerReload;

import java.util.function.Function;

/**
 * @Author:czq
 * @Description:
 * @Date: 15:15 2019/7/26
 * @Modified By:
 */
public class PayloadInfoRes {
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
    private PayloadRule payloadRule;

    /**
     * <pre>
     * ping相关参数
     * This field corresponds to the database column <tt>loadbalance_info.ping_param<tt>
     * </pre>
     */
    private PayloadPing payloadPing;

    /**
     * <pre>
     * 负载器额外参数
     * This field corresponds to the database column <tt>loadbalance_info.lb_ext_param<tt>
     * </pre>
     */
    private PayloadServerReload serverReload;

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

    private Integer lbStatus;

    public String getLbMark() {
        return lbMark;
    }

    public void setLbMark(String lbMark) {
        this.lbMark = lbMark;
    }

    public String getLbName() {
        return lbName;
    }

    public void setLbName(String lbName) {
        this.lbName = lbName;
    }

    public String getLbComment() {
        return lbComment;
    }

    public void setLbComment(String lbComment) {
        this.lbComment = lbComment;
    }

    public String getLbType() {
        return lbType;
    }

    public void setLbType(String lbType) {
        this.lbType = lbType;
    }

    public PayloadRule getPayloadRule() {
        return payloadRule;
    }

    public void setPayloadRule(PayloadRule payloadRule) {
        this.payloadRule = payloadRule;
    }

    public PayloadPing getPayloadPing() {
        return payloadPing;
    }

    public void setPayloadPing(PayloadPing payloadPing) {
        this.payloadPing = payloadPing;
    }

    public PayloadServerReload getServerReload() {
        return serverReload;
    }

    public void setServerReload(PayloadServerReload serverReload) {
        this.serverReload = serverReload;
    }

    public Integer getDscrEnable() {
        return dscrEnable;
    }

    public void setDscrEnable(Integer dscrEnable) {
        this.dscrEnable = dscrEnable;
    }

    public String getDscrId() {
        return dscrId;
    }

    public void setDscrId(String dscrId) {
        this.dscrId = dscrId;
    }

    public Integer getLbStatus() {
        return lbStatus;
    }

    public void setLbStatus(Integer lbStatus) {
        this.lbStatus = lbStatus;
    }
}
