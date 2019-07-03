package com.doctorwork.sword.gateway.service;

import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:17 2019/6/21
 * @Modified By:
 */
public interface GatewayRouteService {

    List<RouteDefinition> routeDefinitions();
}
