package com.doctorwork.sword.gateway.common.event;

/**
 * @author chenzhiqiang
 * @date 2019/7/13
 */
public abstract class RegistryConfigEvent extends RegistryEvent {
    public RegistryConfigEvent(String registryId) {
        super(registryId);
    }
}
