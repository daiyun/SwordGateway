package com.doctorwork.sword.gateway.repository;

import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.service.GatewayRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.handler.predicate.AfterRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.synchronizedMap;

/**
 * @author chenzhiqiang
 * @date 2019/6/21
 */
public class DbRouteDefinitionRepository implements RouteDefinitionRepository {

    private static final Logger logger = LoggerFactory.getLogger(DbRouteDefinitionRepository.class);

    @Autowired
    private GatewayRouteService delegate;

    private final Map<String, RouteDefinition> routes = synchronizedMap(new LinkedHashMap<>());

    public DbRouteDefinitionRepository() {

    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routes.values());
    }

    @EventListener(RefreshRoutesEvent.class)
    public void handleRefresh() {
        List<RouteDefinition> routeDefinitionList = delegate.routeDefinitions();
        logger.info("路由开始重载（路由数量{}）......", routeDefinitionList.size());
        long start = System.currentTimeMillis();
        if (CollectionUtils.isEmpty(routeDefinitionList)) {
            routes.clear();
        } else {
            Map<String, RouteDefinition> routeDefinitionMap = routeDefinitionList.stream().collect(Collectors.toMap(RouteDefinition::getId, Function.identity(), (k1, k2) -> k1));
            Iterator<String> iterator = routes.keySet().iterator();
            Set<String> keyMap = routeDefinitionMap.keySet();
            while (iterator.hasNext()) {
                String k = iterator.next();
                if (!keyMap.contains(k)) {
                    iterator.remove();
                    routes.remove(k);
                }
            }
            for (Map.Entry<String, RouteDefinition> routeDefinitionEntry : routeDefinitionMap.entrySet()) {
                routes.put(routeDefinitionEntry.getKey(), routeDefinitionEntry.getValue());
            }
        }
        long end = System.currentTimeMillis();
        logger.info("路由已重载（路由数量{}）[{}ms]......", routeDefinitionList.size(), end - start);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(r -> {
            routes.put(r.getId(), r);
            logger.info("路由规则{}载入......\n路由规则:{}", r.getId(), JacksonUtil.toJSon(r));
            return Mono.empty();
        });
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id -> {
            if (routes.containsKey(id)) {
                RouteDefinition r = routes.remove(id);
                logger.info("路由规则{}卸载......\n路由规则:{}", r.getId(), JacksonUtil.toJSon(r));
                return Mono.empty();
            }
            return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition not found: " + routeId)));
        });
    }


    public static void main(String[] args) {
        System.out.println(NameUtils.normalizeRoutePredicateName(PathRoutePredicateFactory.class));
        System.out.println(NameUtils.normalizeRoutePredicateName(AfterRoutePredicateFactory.class));
        System.out.println(NameUtils.normalizeRoutePredicateName(PathRoutePredicateFactory.class));
        System.out.println(NameUtils.normalizeRoutePredicateName(PathRoutePredicateFactory.class));
        System.out.println(NameUtils.normalizeRoutePredicateName(PathRoutePredicateFactory.class));
    }
}
