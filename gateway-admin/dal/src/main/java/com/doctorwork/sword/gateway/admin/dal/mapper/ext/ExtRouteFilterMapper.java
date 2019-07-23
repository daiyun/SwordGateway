package com.doctorwork.sword.gateway.admin.dal.mapper.ext;

import com.doctorwork.sword.gateway.admin.dal.model.RouteFilter;

import java.util.List;

public interface ExtRouteFilterMapper {
    List<RouteFilter> get(Long routeId);
}