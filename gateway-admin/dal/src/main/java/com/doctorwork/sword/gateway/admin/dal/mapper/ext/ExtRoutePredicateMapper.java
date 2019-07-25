package com.doctorwork.sword.gateway.admin.dal.mapper.ext;

import com.doctorwork.sword.gateway.admin.dal.model.RoutePredicate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtRoutePredicateMapper {
    RoutePredicate get(Long id);

    List<RoutePredicate> getByRoute(Long routeId);

    int update(@Param("param") RoutePredicate predicate);

    int insert(@Param("param") RoutePredicate predicate);

    int delete(@Param("id") Long id, @Param("routePredicateKey")String routePredicateKey);

    int deleteByRoute(Long routeId);
}