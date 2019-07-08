package com.doctorwork.sword.gateway.discovery.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:czq
 * @Description:
 * @Date: 9:56 2019/5/31
 * @Modified By:
 */
public class ZookeeperInstance {
    private String id;
    private String name;
    private Map<String, String> metadata = new HashMap<>();

    @SuppressWarnings("unused")
    private ZookeeperInstance() {
    }

    public ZookeeperInstance(String id, String name, Map<String, String> metadata) {
        this.id = id;
        this.name = name;
        this.metadata = metadata;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
