package com.doctorwork.sword.gateway.common.event;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:00 2019/7/11
 * @Modified By:
 */
public class LoadBalanceConfigLoadEvent extends LoadBalanceConfigEvent {
    private Integer version;

    public LoadBalanceConfigLoadEvent(String lbMark, int version) {
        super(lbMark);
        this.version = version;
    }

    public Integer getVersion() {
        return version;
    }
}
