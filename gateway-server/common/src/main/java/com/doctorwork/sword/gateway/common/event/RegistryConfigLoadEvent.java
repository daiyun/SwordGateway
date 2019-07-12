package com.doctorwork.sword.gateway.common.event;

import com.doctorwork.sword.gateway.common.config.ConnectionInfo;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:46 2019/7/11
 * @Modified By:
 */
public class RegistryConfigLoadEvent extends RegistryEvent {
    private ConnectionInfo connectionInfo;

    public RegistryConfigLoadEvent(String registryId, ConnectionInfo connectionInfo) {
        super(registryId);
        this.connectionInfo = connectionInfo;
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }
}
