package com.doctorwork.sword.gateway.common.event;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:00 2019/7/11
 * @Modified By:
 */
public abstract class LoadBalanceEvent extends AbstractEvent {
    private final String lbMark;

    protected LoadBalanceEvent(String lbMark) {
        this.lbMark = lbMark;
    }

    public String getLbMark() {
        return lbMark;
    }
}
