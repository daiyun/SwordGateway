package com.doctorwork.sword.gateway.service;

import com.doctorwork.sword.gateway.dal.mapper.ext.ExtRouteFilterMapper;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtRouteInfoMapper;
import com.doctorwork.sword.gateway.dal.mapper.ext.ExtRoutePredicateMapper;
import com.doctorwork.sword.gateway.dal.model.RouteFilter;
import com.doctorwork.sword.gateway.dal.model.RouteInfo;
import com.doctorwork.sword.gateway.dal.model.RoutePredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ProjectName: server-gateway
 * Description: 动态加载路由规则
 * User: wangkai <wangkai@doctorwork.com>
 * Date: 2018/10/18
 * Time: 3:29 PM
 * <p>
 * <p>
 * 因为事件是由事件源发出的，不需要注册为bean由spring容器管理。
 * <p>
 * 所以在spring的配置中，只需配置自定义的ApplicationEventListener和publisherAware(即实现了ApplicationEventPublisherAware接口的发布类)，
 * <p>
 * 而对于ApplicationEventPublisher的管理和注入都由容器来完成。
 * </p>
 */
@Service
public class GatewayRouteService {

    @Autowired
    private ExtRoutePredicateMapper extRoutePredicateMapper;
    @Autowired
    private ExtRouteFilterMapper extRouteFilterMapper;
    @Autowired
    private ExtRouteInfoMapper extRouteInfoMapper;

    public List<RouteDefinition> routeDefinitions() {

        // 第一步是从db获取数据
        List<RouteInfo> routeInfos = extRouteInfoMapper.allEnable();

        if (CollectionUtils.isEmpty(routeInfos)) {
            return Collections.emptyList();
        }
        // for 循环 存储多个
        return routeInfos.stream().map(routeInfo -> {
            RouteDefinition definition = new RouteDefinition();
            definition.setId(String.valueOf(routeInfo.getId()));
            URI uri = UriComponentsBuilder.fromUriString(routeInfo.getRouteUri()).build().toUri();
            definition.setUri(uri);
            definition.setOrder(routeInfo.getRouteSort());
            List<RoutePredicate> routePredicates = extRoutePredicateMapper.get(routeInfo.getId());
            List<RouteFilter> routeFilters = extRouteFilterMapper.get(routeInfo.getId());
            // predicate
            if (!CollectionUtils.isEmpty(routePredicates)) {
                Map<String, List<String>> predicateArgsMap = routePredicates
                        .stream()
                        .collect(Collectors
                                .groupingBy(RoutePredicate::getRoutePredicateKey, Collectors.mapping(RoutePredicate::getRoutePredicateValue, Collectors.toList())));
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
                                .groupingBy(RouteFilter::getRouteFilterKey, Collectors.mapping(RouteFilter::getRouteFilterValue, Collectors.toList())));
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
        }).collect(Collectors.toList());
    }
}
