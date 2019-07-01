package com.doctorwork.sword.gateway.discovery.server;

import com.doctorwork.sword.gateway.discovery.common.AppDiscoveryProperties;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:01 2019/6/19
 * @Modified By:
 */

public class AppRegistryConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(AppRegistryConfiguration.class);
    private AppInstanceRegistration appInstanceRegistration;
    private AppRegistry appRegistry;
    private AppDiscoveryProperties appDiscoveryProperties;
    private AtomicBoolean running = new AtomicBoolean(false);
    private AtomicInteger port = new AtomicInteger(0);

    public AppRegistryConfiguration(ServiceDiscovery<ZookeeperInstance> serviceDiscovery, AppDiscoveryProperties appDiscoveryProperties) {
        ZookeeperInstance zookeeperInstance = new ZookeeperInstance(UUID.randomUUID().toString().replaceAll("-", ""), "test", appDiscoveryProperties.getMetadata());
        AppInstanceRegistration.RegistrationBuilder builder = AppInstanceRegistration.builder()
                .address(appDiscoveryProperties.getHost())
                .name(appDiscoveryProperties.getAppName())
                .payload(zookeeperInstance)
                .uriSpec(appDiscoveryProperties.getUriSpec());
        if (appDiscoveryProperties.getAppSslPort() != null) {
            builder.sslPort(appDiscoveryProperties.getAppSslPort());
        }
        if (appDiscoveryProperties.getAppId() != null) {
            builder.id(appDiscoveryProperties.getAppId());
        }
        this.appInstanceRegistration = builder.build();
        this.appRegistry = new AppRegistry(serviceDiscovery);
        this.appDiscoveryProperties = appDiscoveryProperties;
    }

    public void webInit(Integer port) {
        if (port != null) {
            this.port.compareAndSet(0, port);
        } else {
            if (this.appDiscoveryProperties.getAppPort() != null) {
                this.port.compareAndSet(0, this.appDiscoveryProperties.getAppPort());
            }
        }
        appInstanceRegistration.setPort(this.port.get());
        this.register();
    }

    private void register() {
        if (!this.running.get()) {
            this.appRegistry.register(appInstanceRegistration);
            this.running.compareAndSet(false, true);
        }
    }

    @PreDestroy
    public void destroy() {
        if (this.running.compareAndSet(true, false) && appDiscoveryProperties.isEnabled()) {
            this.appRegistry.deregister(appInstanceRegistration);
            this.appRegistry.close();
        }
    }
}
