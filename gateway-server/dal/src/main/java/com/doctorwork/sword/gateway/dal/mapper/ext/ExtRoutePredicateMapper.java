package com.doctorwork.sword.gateway.dal.mapper.ext;

import com.doctorwork.sword.gateway.dal.model.RoutePredicate;

import java.util.List;

public interface ExtRoutePredicateMapper {
    List<RoutePredicate> get(Long routeId);
}