package com.doctorwork.sword.gateway.discovery.server;

import com.doctorwork.sword.gateway.discovery.common.Constants;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:43 2019/5/31
 * @Modified By:
 */
public class AppIRegistry implements IRegistry<AppInstanceRegistration> {

    private ServiceDiscovery<ZookeeperInstance> serviceDiscovery;

    public AppIRegistry(ServiceDiscovery<ZookeeperInstance> serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public void register(AppInstanceRegistration registration) {
        try {
            this.serviceDiscovery.registerService(registration.getServiceInstance());
        } catch (Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deregister(AppInstanceRegistration registration) {
        try {
            serviceDiscovery.unregisterService(registration.getServiceInstance());
        } catch (Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        try {
            serviceDiscovery.start();
        } catch (Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            serviceDiscovery.close();
        } catch (Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setStatus(AppInstanceRegistration registration, String status) {
        ServiceInstance<ZookeeperInstance> serviceInstance = registration.getServiceInstance();
        ZookeeperInstance instance = serviceInstance.getPayload();
        instance.getMetadata().put(Constants.APP_STATUS_ZK_KEY, status);
        try {
            serviceDiscovery.updateService(serviceInstance);
        } catch (Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getStatus(AppInstanceRegistration registration) {
        ZookeeperInstance instance = registration.getServiceInstance().getPayload();
        return instance.getMetadata().get(Constants.APP_STATUS_ZK_KEY);
    }
}
