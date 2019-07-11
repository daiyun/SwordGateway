package com.doctorwork.sword.gateway.common.event;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:00 2019/7/11
 * @Modified By:
 */
public class LoadBalanceConfigLoadEvent extends LoadBalanceEvent {
    public LoadBalanceConfigLoadEvent(String lbMark) {
        super(lbMark);
    }
}
