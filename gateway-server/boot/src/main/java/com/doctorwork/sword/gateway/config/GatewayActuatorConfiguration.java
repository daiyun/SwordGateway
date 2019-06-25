package com.doctorwork.sword.gateway.config;

import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnEnabledEndpoint;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.gateway.actuate.GatewayControllerEndpoint;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * ProjectName: server-gateway
 * Description: 网关信息配置类
 * User: wangkai <wangkai@doctorwork.com>
 * Date: 2018/10/18
 * Time: 3:19 PM
 */
@Configuration
@ConditionalOnClass(Health.class)
public class GatewayActuatorConfiguration {

    @Bean
    @ConditionalOnEnabledEndpoint
    public GatewayControllerEndpoint gatewayControllerEndpoint(RouteDefinitionLocator routeDefinitionLocator, List<GlobalFilter> globalFilters,
                                                               List<GatewayFilterFactory> GatewayFilters, RouteDefinitionWriter routeDefinitionWriter,
                                                               RouteLocator routeLocator) {
        return new GatewayControllerEndpoint(routeDefinitionLocator, globalFilters, GatewayFilters, routeDefinitionWriter, routeLocator);
    }
}
