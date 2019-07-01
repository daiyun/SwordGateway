package com.doctorwork.sword.gateway.discovery.server;

import java.net.URI;
import java.util.Map;

/**
 * @author chenzhiqiang
 * @date 2019/5/31
 */
public interface AppInstance {
    String getAppId();

    String getHost();

    Integer getPort();

    boolean isSecure();

    URI getUri();

    Map<String, String> getMetadata();

    default String getScheme() {
        return null;
    }
}
