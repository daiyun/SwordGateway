package com.doctorwork.sword.gateway.loadbalance.server;

import com.netflix.loadbalancer.Server;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

/**
 * @Author:czq
 * @Description:
 * @Date: 13:38 2019/7/2
 * @Modified By:
 */
public class CompositiveServerList extends CustomerServerList<AbstractServer> {

    private DataBaseServerList dataBaseServerList;
    private ZookeeperServerList zookeeperServerList;

    public CompositiveServerList(String serviceId, DataBaseServerList dataBaseServerList, ZookeeperServerList zookeeperServerList) {
        super(serviceId);
        if (!dataBaseServerList.getServiceId().equals(zookeeperServerList.getServiceId()))
            throw new RuntimeException("different type");
        this.dataBaseServerList = dataBaseServerList;
        this.zookeeperServerList = zookeeperServerList;
    }

    @Override
    public List<AbstractServer> getInitialListOfServers() {
        return getServers();
    }

    @Override
    public List<AbstractServer> getUpdatedListOfServers() {
        return getServers();
    }

    protected List<AbstractServer> getServers() {
        try {
            List<AbstractServer> servers = new ArrayList<>();
            List<DataBaseServer> dataBaseServers = null;
            List<ZookeeperServer> zookeeperServers = null;
            if (dataBaseServerList != null) {
                dataBaseServers = dataBaseServerList.getInitialListOfServers();
            }
            if (zookeeperServerList != null) {
                zookeeperServers = zookeeperServerList.getInitialListOfServers();
            }
            //如果都配置了则以服务发现列表为准
            if (CollectionUtils.isEmpty(dataBaseServers)) {
                if (!CollectionUtils.isEmpty(zookeeperServers)) {
                    servers.addAll(zookeeperServers);
                    return servers;
                }
            }
            else if (CollectionUtils.isEmpty(zookeeperServers)) {
//                if (!CollectionUtils.isEmpty(dataBaseServers)) {
//                    servers.addAll(dataBaseServers);
//                    return servers;
//                }
                return Collections.emptyList();
            } else {
//                for (DataBaseServer dataBaseServer : dataBaseServers) {
//                    boolean flag = true;
//                    for (ZookeeperServer zookeeperServer : zookeeperServers) {
//                        if (dataBaseServer.getId().equals(zookeeperServer.getId())) {
//                            flag = false;
//                            CompositiveServer compositiveServer = new CompositiveServer(dataBaseServer, zookeeperServer);
//                            servers.add(compositiveServer);
//                        }
//                    }
//                    if (flag)
//                        servers.add(dataBaseServer);
//                }
                for (ZookeeperServer zookeeperServer : zookeeperServers) {
                    boolean flag = true;
                    for (DataBaseServer dataBaseServer : dataBaseServers) {
                        if (dataBaseServer.getId().equals(zookeeperServer.getId())) {
                            flag = false;
                            CompositiveServer compositiveServer = new CompositiveServer(dataBaseServer, zookeeperServer);
                            servers.add(compositiveServer);
                        }
                    }
                    if (flag)
                        servers.add(zookeeperServer);
                }
            }
            return servers;
        } catch (Exception e) {
            rethrowRuntimeException(e);
        }
        return Collections.emptyList();
    }
}
