package com.doctorwork.sword.gateway.common.event;

/**
 * @Author:czq
 * @Description:
 * @Date: 18:31 2019/7/9
 * @Modified By:
 */
public class RegistryLoadEvent extends RegistryEvent {

    private Boolean reload;

    public RegistryLoadEvent(String registryId, Boolean reload) {
        super(registryId);
        this.reload = reload;
    }

    public Boolean getReload() {
        return reload;
    }
}
