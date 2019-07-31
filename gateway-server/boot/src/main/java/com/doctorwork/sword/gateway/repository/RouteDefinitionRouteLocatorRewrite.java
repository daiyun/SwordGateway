package com.doctorwork.sword.gateway.repository;

import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.handler.predicate.RoutePredicateFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionRouteLocator;
import org.springframework.core.convert.ConversionService;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:55 2019/7/31
 * @Modified By:
 */
public class RouteDefinitionRouteLocatorRewrite extends RouteDefinitionRouteLocator {
    public RouteDefinitionRouteLocatorRewrite(RouteDefinitionLocator routeDefinitionLocator, List<RoutePredicateFactory> predicates, List<GatewayFilterFactory> gatewayFilterFactories, GatewayProperties gatewayProperties, ConversionService conversionService) {
        super(routeDefinitionLocator, predicates, gatewayFilterFactories, gatewayProperties, conversionService);

    }

    @Override
    public Flux<Route> getRoutes() {
        return super.getRoutes().onErrorContinue(new BiConsumer<Throwable, Object>() {
            @Override
            public void accept(Throwable throwable, Object o) {
                if (o instanceof RouteDefinition) {
                    RouteDefinition routeDefinition = (RouteDefinition) o;
                    logger.error(String.format("路由{}规则加载错误,忽略该条路由", routeDefinition.getId()), throwable);
                }
            }
        });
    }
}
