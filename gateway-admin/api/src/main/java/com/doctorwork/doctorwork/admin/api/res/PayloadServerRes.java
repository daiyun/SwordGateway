package com.doctorwork.doctorwork.admin.api.res;

/**
 * @author chenzhiqiang
 * @date 2019/7/27
 */
public class PayloadServerRes {
    /**
     * <pre>
     *
     * This field corresponds to the database column <tt>loadbalance_server.id<tt>
     * </pre>
     */
    private String id;

    /**
     * <pre>
     * 负载均衡标识
     * This field corresponds to the database column <tt>loadbalance_server.lb_mark<tt>
     * </pre>
     */
    private String lbMark;

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
     * 备注
     * This field corresponds to the database column <tt>loadbalance_server.comment<tt>
     * </pre>
     */
    private String comment;

    /**
     * 0下线 1上线
     */
    private Integer srvStatus;

    /**
     * 0启用 1禁用
     */
    private Integer srvEnable;

    public PayloadServerRes() {
    }

    public String getLbMark() {
        return lbMark;
    }

    public void setLbMark(String lbMark) {
        this.lbMark = lbMark;
    }

    public String getSrvIp() {
        return srvIp;
    }

    public void setSrvIp(String srvIp) {
        this.srvIp = srvIp;
    }

    public Integer getSrvPort() {
        return srvPort;
    }

    public void setSrvPort(Integer srvPort) {
        this.srvPort = srvPort;
    }

    public String getSrvName() {
        return srvName;
    }

    public void setSrvName(String srvName) {
        this.srvName = srvName;
    }

    public Integer getSrvWeight() {
        return srvWeight;
    }

    public void setSrvWeight(Integer srvWeight) {
        this.srvWeight = srvWeight;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSrvStatus() {
        return srvStatus;
    }

    public void setSrvStatus(Integer srvStatus) {
        this.srvStatus = srvStatus;
    }

    public Integer getSrvEnable() {
        return srvEnable;
    }

    public void setSrvEnable(Integer srvEnable) {
        this.srvEnable = srvEnable;
    }
}
