package com.doctorwork.sword.gateway.dal.mapper.ext;

import com.doctorwork.sword.gateway.dal.model.RouteFilter;

import java.util.List;

public interface ExtRouteFilterMapper {
    List<RouteFilter> get(Long routeId);
}