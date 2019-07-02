package com.doctorwork.sword.gateway.loadbalance.server;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceServer;

import java.util.Objects;

/**
 * @Author:czq
 * @Description:
 * @Date: 18:43 2019/6/10
 * @Modified By:
 */
public class DataBaseServer extends AbstractServer {

    private final LoadbalanceServer loadbalanceServer;

    public DataBaseServer(LoadbalanceServer loadbalanceServer) {
        // TODO: ssl support
        super(loadbalanceServer.getSrvIp(), loadbalanceServer.getSrvPort(), new MetaInfo() {
            @Override
            public String getAppName() {
                return loadbalanceServer.getApolloId();
            }

            @Override
            public String getServerGroup() {
                return null;
            }

            @Override
            public String getServiceIdForDiscovery() {
                return loadbalanceServer.getApolloId();
            }

            @Override
            public String getInstanceId() {
                return String.valueOf(loadbalanceServer.getSrvId());
            }
        });
        this.loadbalanceServer = loadbalanceServer;
    }

    @Override
    public Integer weight() {
        Integer weight = loadbalanceServer.getSrvWeight();
        return weight == null ? 0 : weight;
    }
}
