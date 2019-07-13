package com.doctorwork.sword.gateway.common.event;

import java.io.Closeable;

/**
 * @Author:czq
 * @Description:
 * @Date: 18:31 2019/7/9
 * @Modified By:
 */
public class RegistryLoadEvent extends RegistryEvent {

    private Boolean reload;
    private Closeable closeable;

    public RegistryLoadEvent(String registryId, Boolean reload, Closeable closeable) {
        super(registryId);
        this.reload = reload;
        this.closeable = closeable;
    }

    public Boolean getReload() {
        return reload;
    }

    public void clear() {
        this.closeable = null;
    }

    public Closeable getCloseable() {
        return closeable;
    }
}
