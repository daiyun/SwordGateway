package com.doctorwork.sword.gateway.admin.core;

import com.doctorwork.doctorwork.admin.api.req.*;
import com.doctorwork.doctorwork.admin.api.res.RouteFilterRes;
import com.doctorwork.doctorwork.admin.api.res.RouteInfoRes;
import com.doctorwork.doctorwork.admin.api.res.RoutePredicateRes;
import com.doctorwork.sword.gateway.common.BusinessException;
import com.doctorwork.sword.gateway.common.PageResult;

import java.util.List;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:19 2019/7/23
 * @Modified By:
 */
public interface GatewayRouteService {
    PageResult<RouteInfoRes> routeSearch(RouteSearchReq req);

    RouteInfoRes routeGet(String routeMark) throws BusinessException;

    void routeUpdate(RouteEdit edit) throws BusinessException;

    void routeAdd(RouteEdit edit) throws BusinessException;

    void routeDel(RouteDel del) throws BusinessException;

    void routeEnable(String routeMark);

    void routeDisable(String routeMark);

    List<RoutePredicateRes> routePredication(String routeMark) throws BusinessException;

    void routePredicateUpdate(RoutePredicateEdit edit) throws BusinessException;

    void routePredicateAdd(RoutePredicateEdit edit) throws BusinessException;

    void routePredicateDel(RoutePredicateDel del) throws BusinessException;

    List<RouteFilterRes> routeFilter(String routeMark) throws BusinessException;

    void routeFilterUpdate(RouteFilterEdit edit) throws BusinessException;

    void routeFilterAdd(RouteFilterEdit edit) throws BusinessException;

    void routeFilterDel(RouteFilterDel del) throws BusinessException;
}
