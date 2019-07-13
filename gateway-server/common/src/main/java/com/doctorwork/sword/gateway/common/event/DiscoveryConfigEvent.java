package com.doctorwork.sword.gateway.common.event;

/**
 * @author chenzhiqiang
 * @date 2019/7/13
 */
public abstract class DiscoveryConfigEvent extends DiscoveryEvent {
    public DiscoveryConfigEvent(String dscrId) {
        super(dscrId);
    }
}
