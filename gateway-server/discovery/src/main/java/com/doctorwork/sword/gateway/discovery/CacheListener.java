package com.doctorwork.sword.gateway.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.x.discovery.details.ServiceCacheListener;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:33 2019/7/2
 * @Modified By:
 */
public abstract class CacheListener implements ServiceCacheListener {

    private volatile Boolean reload;

    @Override
    public void cacheChanged() {
        if (reload == null || reload)
            serviceReload();
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        reload = newState.isConnected();
    }

    public abstract void serviceReload();
}
