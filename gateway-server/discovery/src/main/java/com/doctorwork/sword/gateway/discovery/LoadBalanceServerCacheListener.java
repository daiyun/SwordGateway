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
public class LoadBalanceServerCacheListener implements ServiceCacheListener {


    @Override
    public void cacheChanged() {
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {

    }
}
