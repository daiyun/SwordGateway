package com.doctorwork.sword.gateway.common.event;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:16 2019/7/10
 * @Modified By:
 */
public abstract class DiscoveryEvent extends AbstractEvent {
    private final String serviceId;

    public DiscoveryEvent(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return serviceId;
    }
}
