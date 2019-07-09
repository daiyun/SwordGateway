package com.doctorwork.sword.gateway.common.event;

/**
 * @Author:czq
 * @Description:
 * @Date: 18:31 2019/7/9
 * @Modified By:
 */
public class RegistryLoadEvent extends AbstractEvent {
    private String registryId;
    private Boolean reload;

    public RegistryLoadEvent(String registryId, Boolean reload) {
        this.registryId = registryId;
        this.reload = reload;
    }

    public String getRegistryId() {
        return registryId;
    }

    public Boolean getReload() {
        return reload;
    }
}
