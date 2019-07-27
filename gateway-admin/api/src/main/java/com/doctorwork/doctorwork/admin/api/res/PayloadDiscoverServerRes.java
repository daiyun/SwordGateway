package com.doctorwork.doctorwork.admin.api.res;

import java.util.Map;

/**
 * @author chenzhiqiang
 * @date 2019/7/27
 */
public class PayloadDiscoverServerRes {
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

    private Map<String,String> metaData;

    public PayloadDiscoverServerRes() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<String, String> metaData) {
        this.metaData = metaData;
    }
}
