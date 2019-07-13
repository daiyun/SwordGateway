package com.doctorwork.sword.gateway.common.event;

/**
 * @author chenzhiqiang
 * @date 2019/7/8
 */
public class ServiceCacheChangeEvent extends DiscoveryEvent {

    private final String serviceId;

    public ServiceCacheChangeEvent(String serviceId, String dscrId) {
        super(dscrId);
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return serviceId;
    }
}
