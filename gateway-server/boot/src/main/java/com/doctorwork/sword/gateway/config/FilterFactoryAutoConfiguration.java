package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.filter.factory.AccessLogGatewayFilterFactory;
import com.doctorwork.sword.gateway.repository.DbRouteDefinitionRepository;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.route.CachingRouteLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:50 2019/6/26
 * @Modified By:
 */
@Configuration
public class FilterFactoryAutoConfiguration {

    @Bean
    public RouteDefinitionRepository definitionRepository(EventBus eventBus, ObjectProvider<CachingRouteLocator> routeLocator) {
        DbRouteDefinitionRepository dbRouteDefinitionRepository = new DbRouteDefinitionRepository(eventBus, routeLocator);
        return dbRouteDefinitionRepository;
    }

    @Bean
    public AccessLogGatewayFilterFactory accessLogGatewayFilterFactory() {
        return new AccessLogGatewayFilterFactory();
    }
}
