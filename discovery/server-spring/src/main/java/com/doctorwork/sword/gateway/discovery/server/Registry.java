package com.doctorwork.sword.gateway.discovery.server;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author chenzhiqiang
 * @date 2019/7/1
 */
public class Registry implements ApplicationListener<ApplicationEvent> {

    private AppRegistryConfiguration appRegistryConfiguration;

    public Registry(AppRegistryConfiguration appRegistryConfiguration) {
        this.appRegistryConfiguration = appRegistryConfiguration;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            this.appRegistryConfiguration.webInit(null);
        }
    }
}
