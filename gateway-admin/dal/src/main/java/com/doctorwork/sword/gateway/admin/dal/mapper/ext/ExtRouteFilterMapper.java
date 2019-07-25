package com.doctorwork.sword.gateway.admin.dal.mapper.ext;

import com.doctorwork.sword.gateway.admin.dal.model.RouteFilter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExtRouteFilterMapper {
    RouteFilter get(Long id);

    List<RouteFilter> getByRoute(Long routeId);

    int update(@Param("param") RouteFilter routeFilter);

    int insert(@Param("param") RouteFilter routeFilter);

    int delete(@Param("id") Long id, @Param("routeFilterKey") String routeFilterKey);

    int deleteByRoute(Long routeId);
}