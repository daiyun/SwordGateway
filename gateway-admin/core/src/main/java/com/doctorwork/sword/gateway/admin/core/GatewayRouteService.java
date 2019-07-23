package com.doctorwork.sword.gateway.admin.core;

import com.doctorwork.doctorwork.admin.api.req.RouteReq;
import com.doctorwork.doctorwork.admin.api.res.RouteInfoRes;
import com.doctorwork.sword.gateway.common.PageResult;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:19 2019/7/23
 * @Modified By:
 */
public interface GatewayRouteService {
    PageResult<RouteInfoRes> searchRoute(RouteReq req);
}
