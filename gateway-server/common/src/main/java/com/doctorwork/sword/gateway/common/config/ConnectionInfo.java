package com.doctorwork.sword.gateway.common.config;

/**
 * @Author:czq
 * @Description:
 * @Date: 9:21 2019/7/12
 * @Modified By:
 */
public class ConnectionInfo {
    /**
     * 注册中心标识
     */
    private String id;

    /**
     * 注册中心类型
     */
    private String type;

    /**
     * 注册中心配置
     */
    private String config;

    private Integer version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
