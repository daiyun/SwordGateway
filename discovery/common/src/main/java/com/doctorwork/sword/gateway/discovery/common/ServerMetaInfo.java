package com.doctorwork.sword.gateway.discovery.common;

/**
 * @Author:czq
 * @Description:
 * @Date: 18:48 2019/6/10
 * @Modified By:
 */
public interface ServerMetaInfo {
    String getAppName();

    String getServiceIdForDiscovery();

    String getInstanceId();
}
