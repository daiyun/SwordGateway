package com.doctorwork.sword.gateway.admin;

import com.doctorwork.doctorwork.admin.api.req.RoutePredicateEdit;
import com.doctorwork.doctorwork.admin.api.req.RouteReq;
import com.doctorwork.sword.gateway.admin.core.GatewayRouteService;
import com.doctorwork.sword.gateway.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/predication/list")
    @ResponseBody
    public Result publishLoadBalanceConfig(String routeMark) {
        return Result.result(gatewayRouteService.routePredication(routeMark));
    }

    @PostMapping("/predication/edit")
    @ResponseBody
    public Result publishLoadBalanceConfig(RoutePredicateEdit predicateEdit) {
        gatewayRouteService.routePredicateUpdate(predicateEdit);
        return Result.result(null);
    }
}
