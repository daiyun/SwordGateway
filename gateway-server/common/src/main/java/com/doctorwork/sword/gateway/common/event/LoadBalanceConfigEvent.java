package com.doctorwork.sword.gateway.common.event;

/**
 * @author chenzhiqiang
 * @date 2019/7/13
 */
public abstract class LoadBalanceConfigEvent extends LoadBalanceEvent {
    public LoadBalanceConfigEvent(String lbMark) {
        super(lbMark);
    }
}
