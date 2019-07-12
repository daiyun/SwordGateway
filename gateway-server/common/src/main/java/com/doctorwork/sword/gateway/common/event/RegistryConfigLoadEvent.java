package com.doctorwork.sword.gateway.common.event;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:46 2019/7/11
 * @Modified By:
 */
public class RegistryConfigLoadEvent extends RegistryConfigEvent {
    private Integer version;

    public RegistryConfigLoadEvent(String registryId, Integer version) {
        super(registryId);
        this.version = version;
    }

    public Integer getVersion() {
        return version;
    }
}
