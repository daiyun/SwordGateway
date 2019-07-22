package com.doctorwork.sword.gateway.common.config;

import java.util.Collection;

/**
 * @Author:czq
 * @Description:
 * @Date: 15:38 2019/7/22
 * @Modified By:
 */
public interface IRouteConfigRepository {
    RouteInfo routeInfo(String routeMark);

    Collection<RouteInfo> routeInfos();
}
