package com.doctorwork.sword.gateway.dal.mapper.ext;

import com.doctorwork.sword.gateway.dal.model.RouteInfo;

import java.util.List;

public interface ExtRouteInfoMapper {
    List<RouteInfo> allEnable();

    RouteInfo getEnable(String routeMark);
}