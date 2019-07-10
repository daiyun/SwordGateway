package com.doctorwork.sword.gateway.common.event;

/**
 * @author chenzhiqiang
 * @date 2019/7/8
 */
public class ServiceCacheChangeEvent extends DiscoveryEvent {

    public ServiceCacheChangeEvent(String serviceId) {
        super(serviceId);
    }
}
