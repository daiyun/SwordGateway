package com.doctorwork.sword.gateway.admin.dal.mapper.ext;

import com.doctorwork.sword.gateway.admin.dal.model.RouteInfo;

import java.util.List;

public interface ExtRouteInfoMapper {
    List<RouteInfo> list();

    RouteInfo get(String routeMark);
}