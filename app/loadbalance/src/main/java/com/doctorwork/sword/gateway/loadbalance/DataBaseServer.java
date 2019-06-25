package com.doctorwork.sword.gateway.loadbalance;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceServer;
import com.netflix.loadbalancer.Server;

/**
 * @Author:czq
 * @Description:
 * @Date: 18:43 2019/6/10
 * @Modified By:
 */
public class DataBaseServer extends Server {
    private final MetaInfo metaInfo;
    private final LoadbalanceServer loadbalanceServer;

    public DataBaseServer(LoadbalanceServer loadbalanceServer) {
        // TODO: ssl support
        super(loadbalanceServer.getSrvIp(), loadbalanceServer.getSrvPort());
        this.loadbalanceServer = loadbalanceServer;
        this.metaInfo = new MetaInfo() {
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
        };
    }

    @Override
    public MetaInfo getMetaInfo() {
        return this.metaInfo;
    }

    public Integer weight() {
        Integer weight = loadbalanceServer.getSrvWeight();
        return weight == null ? 0 : weight;
    }
}
