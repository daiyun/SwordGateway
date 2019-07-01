package com.doctorwork.sword.gateway.discovery.server;

import com.doctorwork.sword.gateway.discovery.common.Constants;
import com.doctorwork.sword.gateway.discovery.common.DiscoveryProperties;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
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
    private AppIRegistry appRegistry;
    private DiscoveryProperties discoveryProperties;
    private AtomicBoolean running = new AtomicBoolean(false);
    private AtomicInteger port = new AtomicInteger(0);

    public AppRegistryConfiguration(ServiceDiscovery<ZookeeperInstance> serviceDiscovery, DiscoveryProperties discoveryProperties) {
        ZookeeperInstance zookeeperInstance = new ZookeeperInstance(UUID.randomUUID().toString().replaceAll("-", ""), "test", discoveryProperties.getMetadata());
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(Constants.META_INF_APP_PROPERTIES);
        String appId = findAppProperties(inputStream);
        if (!StringUtils.isEmpty(appId)) {
            discoveryProperties.setAppName(appId);
        }
        AppInstanceRegistration.RegistrationBuilder builder = AppInstanceRegistration.builder()
                .address(discoveryProperties.getHost())
                .name(discoveryProperties.getAppName())
                .payload(zookeeperInstance)
                .uriSpec(discoveryProperties.getUriSpec());
        if (discoveryProperties.getAppSslPort() != null) {
            builder.sslPort(discoveryProperties.getAppSslPort());
        }
        if (discoveryProperties.getAppId() != null) {
            builder.id(discoveryProperties.getAppId());
        }
        this.appInstanceRegistration = builder.build();
        this.appRegistry = new AppIRegistry(serviceDiscovery);
        this.discoveryProperties = discoveryProperties;
    }

    public void webInit(Integer port) {
        if (port != null) {
            this.port.compareAndSet(0, port);
        } else {
            if (this.discoveryProperties.getAppPort() != null) {
                this.port.compareAndSet(0, this.discoveryProperties.getAppPort());
            }
        }
        if(this.port.get() == 0){
            throw new RuntimeException("port cannot be 0,plz set the appPort in DiscoveryProperties");
        }
        appInstanceRegistration.setPort(this.port.get());
        this.register();
    }

    private void register() {
        if (!this.running.get()) {
            this.appRegistry.register(appInstanceRegistration);
            this.running.compareAndSet(false, true);
            logger.info("注册服务{},ip:{},port:{}", appInstanceRegistration.getAppId(), appInstanceRegistration.getHost(), appInstanceRegistration.getPort());
        }
    }

    @PreDestroy
    public void destroy() {
        if (this.running.compareAndSet(true, false) && discoveryProperties.isEnabled()) {
            logger.info("注销服务{},ip:{},port:{}", appInstanceRegistration.getAppId(), appInstanceRegistration.getHost(), appInstanceRegistration.getPort());
            this.appRegistry.deregister(appInstanceRegistration);
            this.appRegistry.close();
        }
    }

    public static String findAppProperties(InputStream inputStream) {
        try {
            Properties prop = new Properties();
            prop.load(inputStream);
            Enumeration enums = prop.propertyNames();
            while (enums.hasMoreElements()) {
                String key = (String) enums.nextElement();
                if (Constants.PROPERTIES_APP_ID_KEY.equalsIgnoreCase(key)) {
                    String value = prop.getProperty(key);
                    logger.warn("find appId {}={}", key, value);
                    return value;
                }
            }
        } catch (IOException e) {
            logger.error("exception courred while find appId", e);
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("exception courred while find appId", e);
                }
        }
        return null;
    }
}
