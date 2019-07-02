package com.doctorwork.sword.gateway.loadbalance.server;

/**
 * @Author:czq
 * @Description:
 * @Date: 13:53 2019/7/2
 * @Modified By:
 */
public class CompositiveServer extends AbstractServer {

    private final DataBaseServer dataBaseServer;

    private final ZookeeperServer zookeeperServer;

    public CompositiveServer(DataBaseServer dataBaseServer, ZookeeperServer zookeeperServer) {
        // TODO: ssl support
        super(dataBaseServer.getHost(), dataBaseServer.getPort(), dataBaseServer.getMetaInfo());
        this.dataBaseServer = dataBaseServer;
        this.zookeeperServer = zookeeperServer;
    }

    @Override
    public Integer weight() {
        Integer weight = dataBaseServer.weight();
        if (weight == null)
            return zookeeperServer.weight();
        return weight;
    }
}