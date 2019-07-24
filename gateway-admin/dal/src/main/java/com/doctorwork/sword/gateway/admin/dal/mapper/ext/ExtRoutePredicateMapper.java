package com.doctorwork.sword.gateway.admin.dal.mapper.ext;

import com.doctorwork.sword.gateway.admin.dal.model.RoutePredicate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtRoutePredicateMapper {
    List<RoutePredicate> get(Long routeId);

    int update(@Param("param") RoutePredicate predicate);
}