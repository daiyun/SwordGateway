package com.doctorwork.sword.gateway.admin;

import com.doctorwork.doctorwork.admin.api.req.*;
import com.doctorwork.sword.gateway.admin.core.GatewayRouteService;
import com.doctorwork.sword.gateway.common.BusinessException;
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

    @PostMapping("/list")
    @ResponseBody
    public Result publishLoadBalanceConfig(@RequestBody RouteSearchReq req) {
        return Result.result(gatewayRouteService.routeSearch(req));
    }

    @GetMapping("/get")
    @ResponseBody
    public Result routeGet(String routeMark) throws BusinessException {
        return Result.result(gatewayRouteService.routeGet(routeMark));
    }

    @PostMapping("/edit")
    @ResponseBody
    public Result routeUpdate(@RequestBody RouteEdit predicateEdit) throws BusinessException {
        gatewayRouteService.routeUpdate(predicateEdit);
        return Result.result(null);
    }

    @PostMapping("/add")
    @ResponseBody
    public Result routeAdd(@RequestBody RouteEdit predicateEdit) throws BusinessException {
        gatewayRouteService.routeAdd(predicateEdit);
        return Result.result(null);
    }

    @PostMapping("/del")
    @ResponseBody
    public Result routeDel(@RequestBody RouteDel del) throws BusinessException {
        gatewayRouteService.routeDel(del);
        return Result.result(null);
    }

    @PostMapping("/enable")
    @ResponseBody
    public Result routeEnable(String routeMark) throws BusinessException {
        gatewayRouteService.routeEnable(routeMark);
        return Result.result(null);
    }

    @PostMapping("/disable")
    @ResponseBody
    public Result routeDisable(String routeMark) throws BusinessException {
        gatewayRouteService.routeDisable(routeMark);
        return Result.result(null);
    }

    @GetMapping("/predication/list")
    @ResponseBody
    public Result routePredication(String routeMark) throws BusinessException {
        return Result.result(gatewayRouteService.routePredication(routeMark));
    }

    @PostMapping("/predication/edit")
    @ResponseBody
    public Result routePredicateUpdate(@RequestBody RoutePredicateEdit predicateEdit) throws BusinessException {
        gatewayRouteService.routePredicateUpdate(predicateEdit);
        return Result.result(null);
    }

    @PostMapping("/predication/add")
    @ResponseBody
    public Result routePredicateAdd(@RequestBody RoutePredicateEdit predicateEdit) throws BusinessException {
        gatewayRouteService.routePredicateAdd(predicateEdit);
        return Result.result(null);
    }

    @PostMapping("/predication/del")
    @ResponseBody
    public Result routePredicateAdd(@RequestBody RoutePredicateDel del) throws BusinessException {
        gatewayRouteService.routePredicateDel(del);
        return Result.result(null);
    }

    @GetMapping("/filter/list")
    @ResponseBody
    public Result routeFilter(String routeMark) throws BusinessException {
        return Result.result(gatewayRouteService.routeFilter(routeMark));
    }

    @PostMapping("/filter/edit")
    @ResponseBody
    public Result routeFilterUpdate(@RequestBody RouteFilterEdit edit) throws BusinessException {
        gatewayRouteService.routeFilterUpdate(edit);
        return Result.result(null);
    }

    @PostMapping("/filter/add")
    @ResponseBody
    public Result routeFilterAdd(@RequestBody RouteFilterEdit edit) throws BusinessException {
        gatewayRouteService.routeFilterAdd(edit);
        return Result.result(null);
    }

    @PostMapping("/filter/del")
    @ResponseBody
    public Result routeFilterDel(@RequestBody RouteFilterDel del) throws BusinessException {
        gatewayRouteService.routeFilterDel(del);
        return Result.result(null);
    }
}
