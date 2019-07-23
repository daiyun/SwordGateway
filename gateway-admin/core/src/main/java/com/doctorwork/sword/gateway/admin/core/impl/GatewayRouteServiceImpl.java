package com.doctorwork.sword.gateway.admin.core.impl;

import com.doctorwork.doctorwork.admin.api.req.RouteReq;
import com.doctorwork.doctorwork.admin.api.res.RouteInfoRes;
import com.doctorwork.sword.gateway.admin.core.GatewayRouteService;
import com.doctorwork.sword.gateway.admin.dal.mapper.ext.ExtRouteInfoMapper;
import com.doctorwork.sword.gateway.admin.dal.model.RouteInfo;
import com.doctorwork.sword.gateway.common.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:19 2019/7/23
 * @Modified By:
 */
@Service
public class GatewayRouteServiceImpl implements GatewayRouteService {
    @Autowired
    private ExtRouteInfoMapper extRouteInfoMapper;

    @Override
    public PageResult<RouteInfoRes> searchRoute(RouteReq req) {
        PageHelper.offsetPage(req.getPageNum(), req.getPageSize());
        Page<RouteInfo> page = (Page<RouteInfo>) extRouteInfoMapper.list();
        List<RouteInfoRes> routeInfoRes = page.getResult().stream().map(routeInfo -> {
            RouteInfoRes res = new RouteInfoRes();
            res.setId(routeInfo.getId());
            res.setApolloId(routeInfo.getApolloId());
            res.setRouteComment(routeInfo.getRouteComment());
            res.setRouteMark(routeInfo.getRouteMark());
            res.setRouteName(routeInfo.getRouteName());
            res.setRouteSort(routeInfo.getRouteSort());
            res.setRouteStatus(routeInfo.getRouteStatus());
            res.setRouteTargetMode(routeInfo.getRouteTargetMode());
            res.setRouteUri(routeInfo.getRouteUri());
            return res;
        }).collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), page.getPages(), page.getPageNum(), page.getPageSize(), routeInfoRes);
    }
}
