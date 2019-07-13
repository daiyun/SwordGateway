package com.doctorwork.sword.gateway.loadbalance.server;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:19 2019/7/2
 * @Modified By:
 */
public abstract class CustomerServerList<T extends AbstractServer> extends AbstractServerList<T> {

    protected static final Logger logger = LoggerFactory.getLogger(CustomerServerList.class);

    private String serviceId;

    protected CustomerServerList(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        this.serviceId = clientConfig.getClientName();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public abstract void clear();
}
