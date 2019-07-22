package com.doctorwork.sword.gateway.repository;

import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.common.config.FilterInfo;
import com.doctorwork.sword.gateway.common.config.IRouteConfigRepository;
import com.doctorwork.sword.gateway.common.config.Predication;
import com.doctorwork.sword.gateway.common.config.RouteInfo;
import com.doctorwork.sword.gateway.common.event.RouteConfigDeleteEvent;
import com.doctorwork.sword.gateway.common.event.RouteConfigLoadEvent;
import com.doctorwork.sword.gateway.common.event.RouteEvent;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.synchronizedMap;

/**
 * @author chenzhiqiang
 * @date 2019/6/21
 */
@Component
public class DbRouteDefinitionRepository implements RouteDefinitionRepository, com.doctorwork.sword.gateway.common.listener.EventListener<RouteEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DbRouteDefinitionRepository.class);

    @Autowired
    private IRouteConfigRepository routeConfigRepository;

    private final Map<String, RouteDefinition> routes = synchronizedMap(new LinkedHashMap<>());

    private Function<RouteInfo, RouteDefinition> infoToDefinition = new Function<RouteInfo, RouteDefinition>() {
        @Override
        public RouteDefinition apply(RouteInfo routeInfo) {
            RouteDefinition definition = new RouteDefinition();
            definition.setId(routeInfo.getRouteMark());
            URI uri = UriComponentsBuilder.fromUriString(routeInfo.getRouteUri()).build().toUri();
            definition.setUri(uri);
            definition.setOrder(routeInfo.getRouteSort());
            List<Predication> routePredicates = routeInfo.getPredications();
            List<FilterInfo> routeFilters = routeInfo.getFilters();
            // predicate
            if (!CollectionUtils.isEmpty(routePredicates)) {
                Map<String, List<String>> predicateArgsMap = routePredicates
                        .stream()
                        .collect(Collectors
                                .groupingBy(Predication::getRoutePredicateKey, Collectors.mapping(Predication::getRoutePredicateValue, Collectors.toList())));
                List<PredicateDefinition> predicateDefinitionList = new ArrayList<>(predicateArgsMap.size());
                for (Map.Entry<String, List<String>> predicateArgEntry : predicateArgsMap.entrySet()) {
                    PredicateDefinition predicateDefinition = new PredicateDefinition();
                    predicateDefinition.setName(predicateArgEntry.getKey());
                    List<String> predicateArgs = predicateArgEntry.getValue();
                    for (int i = 0, len = predicateArgs.size(); i < len; i++) {
                        predicateDefinition.getArgs().put(NameUtils.generateName(i), predicateArgs.get(i));
                    }
                    predicateDefinitionList.add(predicateDefinition);
                }
                definition.setPredicates(predicateDefinitionList);
            }

            // filter
            if (!CollectionUtils.isEmpty(routeFilters)) {
                Map<String, List<String>> routeFilterArgsMap = routeFilters
                        .stream()
                        .collect(Collectors
                                .groupingBy(FilterInfo::getRouteFilterKey, Collectors.mapping(FilterInfo::getRouteFilterValue, Collectors.toList())));
                List<FilterDefinition> filterDefinitionList = new ArrayList<>(routeFilterArgsMap.size());
                for (Map.Entry<String, List<String>> filterArgEntry : routeFilterArgsMap.entrySet()) {
                    FilterDefinition filterDefinition = new FilterDefinition();
                    filterDefinition.setName(filterArgEntry.getKey());
                    List<String> filterArgs = filterArgEntry.getValue();
                    for (int i = 0, len = filterArgs.size(); i < len; i++) {
                        filterDefinition.getArgs().put(NameUtils.generateName(i), filterArgs.get(i));
                    }
                    filterDefinitionList.add(filterDefinition);
                }
                definition.setFilters(filterDefinitionList);
            }
            return definition;
        }
    };

    public DbRouteDefinitionRepository(EventBus eventBus) {
        register(eventBus);
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return Flux.fromIterable(routes.values());
    }

    @EventListener(RefreshRoutesEvent.class)
    public void handleRefresh() {
        Collection<RouteInfo> routeInfos = routeConfigRepository.routeInfos();
        logger.info("路由开始重载（路由数量{}）......", routeInfos.size());
        long start = System.currentTimeMillis();
        if (CollectionUtils.isEmpty(routeInfos)) {
            routes.clear();
        } else {
            List<RouteDefinition> routeDefinitions = routeInfos.stream().map(infoToDefinition).collect(Collectors.toList());
            Map<String, RouteDefinition> routeDefinitionMap = routeDefinitions.stream().collect(Collectors.toMap(RouteDefinition::getId, Function.identity(), (k1, k2) -> k1));

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
        logger.info("路由已重载（路由数量{}）[{}ms]......", routeInfos.size(), end - start);
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

    @Override
    public void handleEvent(RouteEvent routeEvent) {
        if (routeEvent instanceof RouteConfigLoadEvent) {
            RouteInfo routeInfo = routeConfigRepository.routeInfo(routeEvent.getRouteMark());
            logger.info("[RouteConfigLoadEvent] handle event for {}", routeEvent.getRouteMark());
            if (routeInfo == null) {
                logger.error("无法加载路由信息{}", routeEvent.getRouteMark());
                return;
            }
            this.save(Mono.just(infoToDefinition.apply(routeInfo)));
        } else if (routeEvent instanceof RouteConfigDeleteEvent) {
            logger.info("[RouteConfigDeleteEvent] handle event for {}", routeEvent.getRouteMark());
            this.delete(Mono.just(routeEvent.getRouteMark()));
        }
    }
}
