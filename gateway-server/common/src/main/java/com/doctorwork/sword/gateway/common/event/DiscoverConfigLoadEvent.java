package com.doctorwork.sword.gateway.common.event;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:49 2019/7/11
 * @Modified By:
 */
public class DiscoverConfigLoadEvent extends DiscoveryConfigEvent {

    private Integer version;

    public DiscoverConfigLoadEvent(String dscrId, Integer version) {
        super(dscrId);
        this.version = version;
    }

    public Integer getVersion() {
        return version;
    }
}
