package com.doctorwork.sword.gateway.loadbalance.server;

import com.doctorwork.sword.gateway.common.config.LoadBalancerServer;

/**
 * @Author:czq
 * @Description:
 * @Date: 18:43 2019/6/10
 * @Modified By:
 */
public class DataBaseServer extends AbstractServer {

    private final LoadBalancerServer server;

    public DataBaseServer(LoadBalancerServer server) {
        // TODO: ssl support
        super(server.getSrvIp(), server.getSrvPort(), new MetaInfo() {
            @Override
            public String getAppName() {
                return server.getSrvName();
            }

            @Override
            public String getServerGroup() {
                return null;
            }

            @Override
            public String getServiceIdForDiscovery() {
                return server.getLbId();
            }

            @Override
            public String getInstanceId() {
                return server.getSrvIp() + ":" + server.getSrvPort();
            }
        });
        this.server = server;
    }

    @Override
    public Integer weight() {
        Integer weight = server.getSrvWeight();
        return weight == null ? 0 : weight;
    }
}
