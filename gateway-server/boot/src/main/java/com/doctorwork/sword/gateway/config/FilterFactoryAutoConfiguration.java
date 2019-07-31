package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.common.config.IRouteConfigRepository;
import com.doctorwork.sword.gateway.filter.factory.AccessLogGatewayFilterFactory;
import com.doctorwork.sword.gateway.repository.DbRouteDefinitionRepository;
import com.doctorwork.sword.gateway.repository.RouteDefinitionRouteLocatorRewrite;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

import java.util.List;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:50 2019/6/26
 * @Modified By:
 */
@Configuration
public class FilterFactoryAutoConfiguration {

    @Bean
    public RouteLocator routeDefinitionRouteLocatorRewrite(GatewayProperties properties,
                                                           List<GatewayFilterFactory> filters,
                                                           List<RoutePredicateFactory> predicates,
                                                           @Qualifier("webFluxConversionService") ConversionService conversionService,
                                                           EventBus eventBus,
                                                           IRouteConfigRepository routeConfigRepository,
                                                           ApplicationEventPublisher publisher) {
        DbRouteDefinitionRepository dbRouteDefinitionRepository = new DbRouteDefinitionRepository(eventBus, routeConfigRepository, publisher);
        RouteDefinitionRouteLocatorRewrite rewrite = new RouteDefinitionRouteLocatorRewrite(dbRouteDefinitionRepository, predicates, filters, properties, conversionService);
        return rewrite;
    }

    @Bean
    public AccessLogGatewayFilterFactory accessLogGatewayFilterFactory() {
        return new AccessLogGatewayFilterFactory();
    }
}
