package com.doctorwork.sword.gateway.common.config;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:42 2019/7/12
 * @Modified By:
 */
public class LoadBalancerServer {
    /**
     * 负载均衡标识
     */
    private String lbId;

    /**
     * 服务IP
     */
    private String srvIp;

    /**
     * 服务端口
     */
    private Integer srvPort;

    /**
     * 服务名称
     */
    private String srvName;

    /**
     * 服务权重
     */
    private Integer srvWeight;

    private Integer srvStatus;

    private Integer srvEnable;

    public String getLbId() {
        return lbId;
    }

    public void setLbId(String lbId) {
        this.lbId = lbId;
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
