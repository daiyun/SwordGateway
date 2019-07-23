package com.doctorwork.sword.gateway.admin;

import com.doctorwork.doctorwork.admin.api.req.RouteReq;
import com.doctorwork.sword.gateway.admin.core.GatewayRouteService;
import com.doctorwork.sword.gateway.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:38 2019/7/23
 * @Modified By:
 */
@RestController
@RequestMapping("route")
public class RouteController {
    @Autowired
    private GatewayRouteService gatewayRouteService;

    @RequestMapping("/list")
    @ResponseBody
    public Result publishLoadBalanceConfig(@RequestBody RouteReq req) {
        return Result.result(gatewayRouteService.searchRoute(req));
    }
}
