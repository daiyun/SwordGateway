package com.doctorwork.sword.gateway.config;

import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenzhiqiang
 * @date 2018/11/6
 */
@Configuration
public class EventBusConfig {
    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }
}
