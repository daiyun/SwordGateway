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

    private ConfigServerList configServerList;
    private ZookeeperServerList zookeeperServerList;
    private Boolean dscrEnable;
    private StampedLock stampedLock = new StampedLock();

    public CompositiveServerList(String serviceId) {
        super(serviceId);
    }

    public CompositiveServerList(String serviceId, ConfigServerList configServerList) {
        super(serviceId);
        this.configServerList = configServerList;
    }

    public CompositiveServerList(String serviceId, Boolean dscrEnable, ConfigServerList configServerList, ZookeeperServerList zookeeperServerList) {
        super(serviceId);
        this.dscrEnable = dscrEnable;
        if (!configServerList.getServiceId().equals(zookeeperServerList.getServiceId()))
            throw new RuntimeException("different type");
        this.configServerList = configServerList;
        this.zookeeperServerList = zookeeperServerList;
    }

    @Override
    public List<AbstractServer> getInitialListOfServers() {
        return getServers(false);
    }

    @Override
    public List<AbstractServer> getUpdatedListOfServers() {
        return getServers(true);
    }

    protected List<AbstractServer> getServers(boolean update) {
        try {
            boolean valid = true;
            long stamp = stampedLock.tryOptimisticRead();
            List<AbstractServer> servers = new ArrayList<>();
            compsitiveServerList(valid, update, servers);
            if (!stampedLock.validate(stamp)) {
                stampedLock.readLock();
                try {
                    servers.clear();
                    compsitiveServerList(valid, update, servers);
                } finally {
                    stampedLock.unlockRead(stamp);
                }
            }
            return servers;
        } catch (Exception e) {
            rethrowRuntimeException(e);
        }
        return Collections.emptyList();
    }

    private void compsitiveServerList(boolean valid, boolean update, List<AbstractServer> servers) {
        List<ConfigServer> configServers = null;
        List<ZookeeperServer> zookeeperServers;
        if (configServerList != null) {
            if (valid) {
                configServers = update ? configServerList.getUpdatedListOfServers() : configServerList.getInitialListOfServers();
            } else {
                configServers = configServerList.getServer(valid);
            }
        }
        if (dscrEnable != null && dscrEnable) {
            if (zookeeperServerList == null)
                return;
            if (valid) {
                zookeeperServers = update ? zookeeperServerList.getUpdatedListOfServers() : zookeeperServerList.getInitialListOfServers();
            } else {
                zookeeperServers = zookeeperServerList.getServer(valid);
            }
            if (CollectionUtils.isEmpty(zookeeperServers)) {
                return;
            }
            if (CollectionUtils.isEmpty(configServers)) {
                servers.addAll(zookeeperServers);
                return;
            }
            for (ZookeeperServer zookeeperServer : zookeeperServers) {
                boolean flag = true;
                for (ConfigServer configServer : configServers) {
                    if (configServer.getId().equals(zookeeperServer.getId())) {
                        flag = false;
                        CompositiveServer compositiveServer = new CompositiveServer(configServer, zookeeperServer);
                        servers.add(compositiveServer);
                    }
                }
                if (flag)
                    servers.add(zookeeperServer);
            }
            return;
        }
        if (!CollectionUtils.isEmpty(configServers)) {
            servers.addAll(configServers);
        }
    }

    public Boolean getDscrEnable() {
        return dscrEnable;
    }

    public void discoveryReload(Boolean dscrEnable, ConfigServerList configServerList, ZookeeperServerList zookeeperServerList) {
        long stamp = stampedLock.writeLock();
        try {
            this.configServerList = configServerList;
            this.dscrEnable = true;
            if (dscrEnable != null && dscrEnable) {
                this.zookeeperServerList = zookeeperServerList;
            } else {
                this.zookeeperServerList = null;
            }
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    @Override
    public List<AbstractServer> getServer(boolean valid) {
        List<AbstractServer> servers = new ArrayList<>();
        compsitiveServerList(true, true, servers);
        return servers;
    }

    @Override
    public void clear() {
        if (configServerList != null) {
            configServerList.clear();
        }
        if (zookeeperServerList != null) {
            zookeeperServerList.clear();
        }
        this.dscrEnable = null;
        this.stampedLock = null;
        super.setServiceId(null);
    }
}
