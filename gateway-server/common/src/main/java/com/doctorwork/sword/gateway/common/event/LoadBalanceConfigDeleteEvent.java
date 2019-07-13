package com.doctorwork.sword.gateway.common.event;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:01 2019/7/11
 * @Modified By:
 */
public class LoadBalanceConfigDeleteEvent extends LoadBalanceConfigEvent {
    public LoadBalanceConfigDeleteEvent(String lbMark) {
        super(lbMark);
    }
}
