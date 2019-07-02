package com.doctorwork.sword.gateway.loadbalance.server;

import com.netflix.loadbalancer.Server;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:11 2019/7/2
 * @Modified By:
 */
public abstract class AbstractServer extends Server {
    private final MetaInfo metaInfo;

    protected AbstractServer(String ip, Integer port, MetaInfo metaInfo) {
        super(ip, port);
        this.metaInfo = metaInfo;
    }

    @Override
    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public abstract Integer weight();
}
