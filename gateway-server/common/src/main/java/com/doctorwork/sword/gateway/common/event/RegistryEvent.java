package com.doctorwork.sword.gateway.common.event;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:14 2019/7/10
 * @Modified By:
 */
public abstract class RegistryEvent extends AbstractEvent {
    private final String registryId;

    public RegistryEvent(String registryId) {
        this.registryId = registryId;
    }

    public String getRegistryId() {
        return registryId;
    }
}
