package com.doctorwork.sword.gateway.common.event;

/**
 * @Author:czq
 * @Description:
 * @Date: 18:31 2019/7/9
 * @Modified By:
 */
public class RegistryLoadEvent extends RegistryEvent {

    private Boolean reload;
    private EventCall<Void> eventCall;

    public RegistryLoadEvent(String registryId, Boolean reload, EventCall<Void> eventCall) {
        super(registryId);
        this.reload = reload;
        this.eventCall = eventCall;
    }

    public Boolean getReload() {
        return reload;
    }

    public synchronized void eventCall() {
        if (eventCall != null) {
            eventCall.call();
            this.eventCall = null;
        }
    }
}
