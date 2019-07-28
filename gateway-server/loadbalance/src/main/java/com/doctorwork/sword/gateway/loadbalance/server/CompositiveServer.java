package com.doctorwork.sword.gateway.loadbalance.server;

/**
 * @Author:czq
 * @Description:
 * @Date: 13:53 2019/7/2
 * @Modified By:
 */
public class CompositiveServer extends AbstractServer {

    private final ConfigServer configServer;

    private final ZookeeperServer zookeeperServer;

    public CompositiveServer(ConfigServer configServer, ZookeeperServer zookeeperServer) {
        // TODO: ssl support
        super(configServer.getHost(), configServer.getPort(), configServer.getMetaInfo());
        this.configServer = configServer;
        this.zookeeperServer = zookeeperServer;
    }

    @Override
    public Integer weight() {
        Integer weight = configServer.weight();
        if (weight == null)
            return zookeeperServer.weight();
        return weight;
    }
}