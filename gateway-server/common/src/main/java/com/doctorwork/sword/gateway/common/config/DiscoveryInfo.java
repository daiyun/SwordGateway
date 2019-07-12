package com.doctorwork.sword.gateway.common.config;

/**
 * @Author:czq
 * @Description:
 * @Date: 9:23 2019/7/12
 * @Modified By:
 */
public class DiscoveryInfo {
    /**
     * 服务发现配置
     */
    private String id;

    /**
     * 服务发现类型
     */
    private String type;

    /**
     * 服务发现配置
     */
    private String config;

    /**
     * 注册中心配置标识
     */
    private String conectionId;

    /**
     * 是否启用预加载 0默认启用 1不启动
     */
    private Integer preload;

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

    public String getConectionId() {
        return conectionId;
    }

    public void setConectionId(String conectionId) {
        this.conectionId = conectionId;
    }

    public Integer getPreload() {
        return preload;
    }

    public void setPreload(Integer preload) {
        this.preload = preload;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
