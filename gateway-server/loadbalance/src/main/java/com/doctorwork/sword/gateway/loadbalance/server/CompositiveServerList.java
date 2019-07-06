package com.doctorwork.sword.gateway.loadbalance.server;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.StampedLock;

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
    private StampedLock stampedLock = new StampedLock();

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
            long stamp = stampedLock.tryOptimisticRead();
            List<AbstractServer> servers = new ArrayList<>();
            compsitiveServerList(servers);
            if (!stampedLock.validate(stamp)) {
                stampedLock.readLock();
                try {
                    servers.clear();
                    compsitiveServerList(servers);
                } finally {
                    stampedLock.unlockRead(stamp);
                }
            }
        } catch (Exception e) {
            rethrowRuntimeException(e);
        }
        return Collections.emptyList();
    }

    private void compsitiveServerList(List<AbstractServer> servers) {
        List<DataBaseServer> dataBaseServers = null;
        List<ZookeeperServer> zookeeperServers;
        if (dataBaseServerList != null) {
            dataBaseServers = dataBaseServerList.getInitialListOfServers();
        }
        if (dscrEnable != null && dscrEnable) {
            if (zookeeperServerList == null)
                return;
            zookeeperServers = zookeeperServerList.getInitialListOfServers();
            if (CollectionUtils.isEmpty(zookeeperServers)) {
                return;
            }
            if (CollectionUtils.isEmpty(dataBaseServers)) {
                servers.addAll(zookeeperServers);
                return;
            }
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
            return;
        }
        if (!CollectionUtils.isEmpty(dataBaseServers)) {
            servers.addAll(dataBaseServers);
        }
    }

    public Boolean getDscrEnable() {
        return dscrEnable;
    }

    public void discoveryReload(Boolean dscrEnable, DataBaseServerList dataBaseServerList, ZookeeperServerList zookeeperServerList) {
        long stamp = stampedLock.writeLock();
        try {
            this.dataBaseServerList = dataBaseServerList;
            this.dscrEnable = true;
            if (dscrEnable != null && dscrEnable) {
                this.zookeeperServerList = zookeeperServerList;
            }
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }
}
