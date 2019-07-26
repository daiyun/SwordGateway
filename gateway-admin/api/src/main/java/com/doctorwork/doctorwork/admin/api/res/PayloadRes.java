package com.doctorwork.doctorwork.admin.api.res;

/**
 * @Author:czq
 * @Description:
 * @Date: 14:19 2019/7/26
 * @Modified By:
 */
public class PayloadRes {
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
