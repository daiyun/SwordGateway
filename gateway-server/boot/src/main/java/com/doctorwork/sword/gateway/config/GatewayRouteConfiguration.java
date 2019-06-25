package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.repository.DbRouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenzhiqiang
 * @date 2019/6/21
 */
@Configuration
public class GatewayRouteConfiguration {

    @Bean
    public RouteDefinitionRepository cachingRouteDefinitionLocator() {
        return new DbRouteDefinitionRepository();
    }
}
