package com.doctorwork.sword.gateway.service.impl;

import com.doctorwork.sword.gateway.common.config.FilterInfo;
import com.doctorwork.sword.gateway.common.config.Predication;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtRouteFilterMapper;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtRouteInfoMapper;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtRoutePredicateMapper;
import com.doctorwork.sword.gateway.dal.model.RouteFilter;
import com.doctorwork.sword.gateway.dal.model.RouteInfo;
import com.doctorwork.sword.gateway.dal.model.RoutePredicate;
import com.doctorwork.sword.gateway.service.GatewayRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenzhiqiang
 * @date 2019/7/3
 */
@Service
public class GatewayRouteServiceImpl implements GatewayRouteService {

    @Autowired
    private ExtRoutePredicateMapper extRoutePredicateMapper;
    @Autowired
    private ExtRouteFilterMapper extRouteFilterMapper;
    @Autowired
    private ExtRouteInfoMapper extRouteInfoMapper;

    @Override
    public com.doctorwork.sword.gateway.common.config.RouteInfo routeDefinition(String routeMark) {

        // 第一步是从db获取数据
        RouteInfo routeInfo = extRouteInfoMapper.getEnable(routeMark);

        if (routeInfo == null) {
            return null;
        }
        com.doctorwork.sword.gateway.common.config.RouteInfo route = new com.doctorwork.sword.gateway.common.config.RouteInfo();
        route.setRouteMark(routeInfo.getRouteMark());
        route.setRouteName(routeInfo.getRouteName());
        route.setRouteSort(routeInfo.getRouteSort());
        route.setRouteUri(routeInfo.getRouteUri());
        List<RoutePredicate> routePredicates = extRoutePredicateMapper.get(routeInfo.getId());
        List<RouteFilter> routeFilters = extRouteFilterMapper.get(routeInfo.getId());
        if (!CollectionUtils.isEmpty(routePredicates)) {
            route.setPredications(routePredicates.stream().map(routePredicate -> {
                Predication predication = new Predication();
                predication.setRoutePredicateKey(routePredicate.getRoutePredicateKey());
                predication.setRoutePredicateValue(routePredicate.getRoutePredicateValue());
                return predication;
            }).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(routeFilters)) {
            route.setFilters(routeFilters.stream().map(routeFilter -> {
                FilterInfo filterInfo = new FilterInfo();
                filterInfo.setRouteFilterKey(routeFilter.getRouteFilterKey());
                filterInfo.setRouteFilterValue(routeFilter.getRouteFilterValue());
                return filterInfo;
            }).collect(Collectors.toList()));
        }
        return route;
    }

    @Override
    public List<com.doctorwork.sword.gateway.common.config.RouteInfo> routeDefinitions() {

        // 第一步是从db获取数据
        List<RouteInfo> routeInfos = extRouteInfoMapper.allEnable();

        if (CollectionUtils.isEmpty(routeInfos)) {
            return Collections.emptyList();
        }
        // for 循环 存储多个
        return routeInfos.stream().map(routeInfo -> {
            com.doctorwork.sword.gateway.common.config.RouteInfo route = new com.doctorwork.sword.gateway.common.config.RouteInfo();
            route.setRouteMark(routeInfo.getRouteMark());
            route.setRouteName(routeInfo.getRouteName());
            route.setRouteSort(routeInfo.getRouteSort());
            route.setRouteUri(routeInfo.getRouteUri());
            List<RoutePredicate> routePredicates = extRoutePredicateMapper.get(routeInfo.getId());
            List<RouteFilter> routeFilters = extRouteFilterMapper.get(routeInfo.getId());
            if (!CollectionUtils.isEmpty(routePredicates)) {
                route.setPredications(routePredicates.stream().map(routePredicate -> {
                    Predication predication = new Predication();
                    predication.setRoutePredicateKey(routePredicate.getRoutePredicateKey());
                    predication.setRoutePredicateValue(routePredicate.getRoutePredicateValue());
                    return predication;
                }).collect(Collectors.toList()));
            }
            if (!CollectionUtils.isEmpty(routeFilters)) {
                route.setFilters(routeFilters.stream().map(routeFilter -> {
                    FilterInfo filterInfo = new FilterInfo();
                    filterInfo.setRouteFilterKey(routeFilter.getRouteFilterKey());
                    filterInfo.setRouteFilterValue(routeFilter.getRouteFilterValue());
                    return filterInfo;
                }).collect(Collectors.toList()));
            }
            return route;
        }).collect(Collectors.toList());
    }
//        RouteDefinition definition = new RouteDefinition();
//        definition.setId(String.valueOf(routeInfo.getId()));
//        URI uri = UriComponentsBuilder.fromUriString(routeInfo.getRouteUri()).build().toUri();
//        definition.setUri(uri);
//        definition.setOrder(routeInfo.getRouteSort());
//        List<RoutePredicate> routePredicates = extRoutePredicateMapper.get(routeInfo.getId());
//        List<RouteFilter> routeFilters = extRouteFilterMapper.get(routeInfo.getId());
//        // predicate
//        if (!CollectionUtils.isEmpty(routePredicates)) {
//            Map<String, List<String>> predicateArgsMap = routePredicates
//                    .stream()
//                    .collect(Collectors
//                            .groupingBy(RoutePredicate::getRoutePredicateKey, Collectors.mapping(RoutePredicate::getRoutePredicateValue, Collectors.toList())));
//            List<PredicateDefinition> predicateDefinitionList = new ArrayList<>(predicateArgsMap.size());
//            for (Map.Entry<String, List<String>> predicateArgEntry : predicateArgsMap.entrySet()) {
//                PredicateDefinition predicateDefinition = new PredicateDefinition();
//                predicateDefinition.setName(predicateArgEntry.getKey());
//                List<String> predicateArgs = predicateArgEntry.getValue();
//                for (int i = 0, len = predicateArgs.size(); i < len; i++) {
//                    predicateDefinition.getArgs().put(NameUtils.generateName(i), predicateArgs.get(i));
//                }
//                predicateDefinitionList.add(predicateDefinition);
//            }
//            definition.setPredicates(predicateDefinitionList);
//        }
//
//        // filter
//        if (!CollectionUtils.isEmpty(routeFilters)) {
//            Map<String, List<String>> routeFilterArgsMap = routeFilters
//                    .stream()
//                    .collect(Collectors
//                            .groupingBy(RouteFilter::getRouteFilterKey, Collectors.mapping(RouteFilter::getRouteFilterValue, Collectors.toList())));
//            List<FilterDefinition> filterDefinitionList = new ArrayList<>(routeFilterArgsMap.size());
//            for (Map.Entry<String, List<String>> filterArgEntry : routeFilterArgsMap.entrySet()) {
//                FilterDefinition filterDefinition = new FilterDefinition();
//                filterDefinition.setName(filterArgEntry.getKey());
//                List<String> filterArgs = filterArgEntry.getValue();
//                for (int i = 0, len = filterArgs.size(); i < len; i++) {
//                    filterDefinition.getArgs().put(NameUtils.generateName(i), filterArgs.get(i));
//                }
//                filterDefinitionList.add(filterDefinition);
//            }
//            definition.setFilters(filterDefinitionList);
//        }
//        return definition;
}
