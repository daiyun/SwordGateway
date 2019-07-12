package com.doctorwork.sword.gateway.common;

/**
 * @Author:czq
 * @Description:
 * @Date: 9:25 2019/7/12
 * @Modified By:
 */
public class LoadBalancerInfo {

    /**
     * 负载标识
     */
    private String id;

    /**
     * 负载器名称
     */
    private String name;

    /**
     * 负载器类型
     */
    private String type;

    /**
     * 负载规则参数
     */
    private String ruleParam;

    /**
     * ping相关参数
     */
    private String pingParam;

    /**
     * 负载器额外参数
     */
    private String lbExtParam;

    /**
     * 是否采用服务发现: 0默认不可用 1可用
     */
    private Integer dscrEnable;

    /**
     * 服务发现标识
     */
    private String discoveryId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRuleParam() {
        return ruleParam;
    }

    public void setRuleParam(String ruleParam) {
        this.ruleParam = ruleParam;
    }

    public String getPingParam() {
        return pingParam;
    }

    public void setPingParam(String pingParam) {
        this.pingParam = pingParam;
    }

    public String getLbExtParam() {
        return lbExtParam;
    }

    public void setLbExtParam(String lbExtParam) {
        this.lbExtParam = lbExtParam;
    }

    public Integer getDscrEnable() {
        return dscrEnable;
    }

    public void setDscrEnable(Integer dscrEnable) {
        this.dscrEnable = dscrEnable;
    }

    public String getDiscoveryId() {
        return discoveryId;
    }

    public void setDiscoveryId(String discoveryId) {
        this.discoveryId = discoveryId;
    }
}
