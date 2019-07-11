package com.doctorwork.sword.gateway.common.event;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:16 2019/7/10
 * @Modified By:
 */
public abstract class DiscoveryEvent extends AbstractEvent {
    private final String dscrId;

    public DiscoveryEvent(String dscrId) {
        this.dscrId = dscrId;
    }

    public String getDscrId() {
        return dscrId;
    }
}
