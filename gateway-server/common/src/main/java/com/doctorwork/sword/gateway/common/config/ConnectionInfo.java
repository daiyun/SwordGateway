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

    /**
     * 格式（标识-类型-注册中心配置hashValue-时间戳）
     */
    private String hash;

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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
