package com.doctorwork.sword.gateway.common.event;

/**
 * @author chenzhiqiang
 * @date 2019/7/8
 */
public class ServiceCacheChangeEvent extends DiscoveryEvent {

    private final String serviceId;

    public ServiceCacheChangeEvent(String serviceId, String serviceId1) {
        super(serviceId);
        this.serviceId = serviceId1;
    }

    public String getServiceId() {
        return serviceId;
    }
}
