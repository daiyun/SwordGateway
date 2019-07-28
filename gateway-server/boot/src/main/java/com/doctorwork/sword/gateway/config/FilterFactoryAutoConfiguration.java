package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.filter.factory.AccessLogGatewayFilterFactory;
import com.doctorwork.sword.gateway.repository.DbRouteDefinitionRepository;
import com.google.common.eventbus.EventBus;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
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
    public RouteDefinitionRepository definitionRepository(EventBus eventBus){
        DbRouteDefinitionRepository dbRouteDefinitionRepository = new DbRouteDefinitionRepository(eventBus);
        return dbRouteDefinitionRepository;
    }
    @Bean
    public AccessLogGatewayFilterFactory accessLogGatewayFilterFactory() {
        return new AccessLogGatewayFilterFactory();
    }
}
