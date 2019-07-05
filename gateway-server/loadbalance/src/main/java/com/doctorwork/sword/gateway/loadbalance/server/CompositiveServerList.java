package com.doctorwork.sword.gateway.loadbalance.server;

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
    private Boolean dscrEnable;

    public CompositiveServerList(String serviceId) {
        super(serviceId);
    }

    public CompositiveServerList(String serviceId, DataBaseServerList dataBaseServerList) {
        super(serviceId);
        this.dataBaseServerList = dataBaseServerList;
    }

    public CompositiveServerList(String serviceId, Boolean dscrEnable, DataBaseServerList dataBaseServerList, ZookeeperServerList zookeeperServerList) {
        super(serviceId);
        this.dscrEnable = dscrEnable;
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

            if (dscrEnable != null && dscrEnable) {
                if (zookeeperServerList == null)
                    return Collections.emptyList();
                zookeeperServers = zookeeperServerList.getInitialListOfServers();
                if (CollectionUtils.isEmpty(dataBaseServers)) {
                    if (!CollectionUtils.isEmpty(zookeeperServers)) {
                        servers.addAll(zookeeperServers);
                        return servers;
                    }
                } else if (CollectionUtils.isEmpty(zookeeperServers)) {
                    return Collections.emptyList();
                } else {
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
            }
            if (!CollectionUtils.isEmpty(dataBaseServers)) {
                servers.addAll(dataBaseServers);
            }
            return servers;
        } catch (Exception e) {
            rethrowRuntimeException(e);
        }
        return Collections.emptyList();
    }

    public Boolean getDscrEnable() {
        return dscrEnable;
    }

    public void setDscrEnable(Boolean dscrEnable) {
        this.dscrEnable = dscrEnable;
    }
}
