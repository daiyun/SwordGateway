package com.doctorwork.sword.gateway.admin.core.impl;

import com.doctorwork.doctorwork.admin.api.req.*;
import com.doctorwork.doctorwork.admin.api.res.RouteFilterRes;
import com.doctorwork.doctorwork.admin.api.res.RouteInfoRes;
import com.doctorwork.doctorwork.admin.api.res.RoutePredicateRes;
import com.doctorwork.sword.gateway.admin.core.GatewayRouteService;
import com.doctorwork.sword.gateway.admin.dal.mapper.ext.ExtRouteFilterMapper;
import com.doctorwork.sword.gateway.admin.dal.mapper.ext.ExtRouteInfoMapper;
import com.doctorwork.sword.gateway.admin.dal.mapper.ext.ExtRoutePredicateMapper;
import com.doctorwork.sword.gateway.admin.dal.model.RouteFilter;
import com.doctorwork.sword.gateway.admin.dal.model.RouteInfo;
import com.doctorwork.sword.gateway.admin.dal.model.RoutePredicate;
import com.doctorwork.sword.gateway.common.BusinessException;
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
    @Autowired
    private ExtRoutePredicateMapper extRoutePredicateMapper;
    @Autowired
    private ExtRouteFilterMapper extRouteFilterMapper;

    @Override
    public PageResult<RouteInfoRes> routeSearch(RouteSearchReq req) {
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

    @Override
    public RouteInfoRes routeGet(String routeMark) throws BusinessException {
        RouteInfo routeInfo = extRouteInfoMapper.get(routeMark);
        if (routeInfo == null)
            throw new BusinessException("未找到路由信息");
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
    }

    @Override
    public void routeUpdate(RouteEdit edit) throws BusinessException {
        RouteInfo routeInfo = extRouteInfoMapper.get(edit.getRouteMark());
        if (routeInfo == null)
            throw new BusinessException("未找到路由信息");
        RouteInfo update = new RouteInfo();
        update.setRouteMark(edit.getRouteMark());
        update.setRouteComment(edit.getRouteComment());
        update.setRouteName(edit.getRouteName());
        update.setRouteSort(edit.getRouteSort());
        update.setRouteTargetMode(edit.getRouteTargetMode());
        update.setRouteUri(edit.getRouteUri());
        extRouteInfoMapper.update(update);
    }

    @Override
    public void routeAdd(RouteEdit edit) throws BusinessException {
        RouteInfo routeInfo = extRouteInfoMapper.get(edit.getRouteMark());
        if (routeInfo != null)
            throw new BusinessException("路由信息已存在");
        RouteInfo insert = new RouteInfo();
        insert.setRouteMark(edit.getRouteMark());
        insert.setRouteComment(edit.getRouteComment());
        insert.setRouteName(edit.getRouteName());
        insert.setRouteSort(edit.getRouteSort());
        insert.setRouteTargetMode(edit.getRouteTargetMode());
        insert.setRouteUri(edit.getRouteUri());
        insert.setRouteStatus(0);//新增的路由信息定义为未启用
        extRouteInfoMapper.insert(insert);
    }

    @Override
    public void routeDel(RouteDel del) throws BusinessException {
        RouteInfo routeInfo = extRouteInfoMapper.get(del.getRouteMark());
        if (routeInfo == null)
            throw new BusinessException("未找到路由信息");
        //禁用路由
        routeDisable(del.getRouteMark());
        //删除路由
        extRouteInfoMapper.delete(del.getRouteMark());
        //删除关联匹配规则和过滤规则
        extRoutePredicateMapper.deleteByRoute(routeInfo.getId());
        extRouteFilterMapper.deleteByRoute(routeInfo.getId());
    }

    @Override
    public void routeEnable(String routeMark) throws BusinessException {
        RouteInfo routeInfo = extRouteInfoMapper.get(routeMark);
        if (routeInfo == null)
            throw new BusinessException("未找到路由信息");
        //发布配置
        extRouteInfoMapper.updateStatus(routeMark, 1);
    }

    @Override
    public void routeDisable(String routeMark) throws BusinessException {
        RouteInfo routeInfo = extRouteInfoMapper.get(routeMark);
        if (routeInfo == null)
            throw new BusinessException("未找到路由信息");
        //发布配置
        extRouteInfoMapper.updateStatus(routeMark, 2);
    }

    @Override
    public List<RoutePredicateRes> routePredication(String routeMark) throws BusinessException {
        RouteInfo routeInfo = extRouteInfoMapper.get(routeMark);
        if (routeInfo == null)
            throw new BusinessException("未找到路由信息");
        List<RoutePredicate> predicates = extRoutePredicateMapper.getByRoute(routeInfo.getId());
        return predicates.stream().map(routePredicate -> {
            RoutePredicateRes res = new RoutePredicateRes();
            res.setId(String.valueOf(routePredicate.getId()));
            res.setRoutePredicateKey(routePredicate.getRoutePredicateKey());
            res.setRoutePredicateValue(routePredicate.getRoutePredicateValue());
            res.setRoutePredicateComment(routePredicate.getRoutePredicateComment());
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public void routePredicateUpdate(RoutePredicateEdit predicateEdit) throws BusinessException {
        RoutePredicate predicate = extRoutePredicateMapper.get(Long.valueOf(predicateEdit.getId()));
        if (predicate == null)
            throw new BusinessException("未找到匹配信息");
        RoutePredicate update = new RoutePredicate();
        update.setId(predicate.getId());
        update.setRoutePredicateKey(predicateEdit.getRoutePredicateKey());
        update.setRoutePredicateValue(predicateEdit.getRoutePredicateValue());
        update.setRoutePredicateComment(predicateEdit.getRoutePredicateComment());
        extRoutePredicateMapper.update(update);
    }

    @Override
    public void routePredicateAdd(RoutePredicateEdit predicateEdit) throws BusinessException {
        RouteInfo routeInfo = extRouteInfoMapper.get(predicateEdit.getRouteMark());
        if (routeInfo == null)
            throw new BusinessException("未找到路由信息");
        RoutePredicate insert = new RoutePredicate();
        insert.setRouteId(routeInfo.getId());
        insert.setRoutePredicateKey(predicateEdit.getRoutePredicateKey());
        insert.setRoutePredicateValue(predicateEdit.getRoutePredicateValue());
        insert.setRoutePredicateComment(predicateEdit.getRoutePredicateComment());
        extRoutePredicateMapper.insert(insert);
    }

    @Override
    public void routePredicateDel(RoutePredicateDel del) throws BusinessException {
        RoutePredicate predicate = extRoutePredicateMapper.get(Long.valueOf(del.getId()));
        if (predicate == null)
            throw new BusinessException("未找到匹配信息");
        extRoutePredicateMapper.delete(predicate.getId(), del.getRoutePredicateKey());
    }

    @Override
    public List<RouteFilterRes> routeFilter(String routeMark) throws BusinessException {
        RouteInfo routeInfo = extRouteInfoMapper.get(routeMark);
        if (routeInfo == null)
            throw new BusinessException("未找到路由信息");
        List<RouteFilter> filters = extRouteFilterMapper.getByRoute(routeInfo.getId());
        return filters.stream().map(routeFilter -> {
            RouteFilterRes res = new RouteFilterRes();
            res.setId(String.valueOf(routeFilter.getId()));
            res.setRouteFilterKey(routeFilter.getRouteFilterKey());
            res.setRouteFilterValue(routeFilter.getRouteFilterValue());
            res.setRouteFilterComment(routeFilter.getRouteFilterComment());
            return res;
        }).collect(Collectors.toList());
    }

    @Override
    public void routeFilterUpdate(RouteFilterEdit edit) throws BusinessException {
        RouteFilter filter = extRouteFilterMapper.get(Long.valueOf(edit.getId()));
        if (filter == null)
            throw new BusinessException("未找到过滤器信息");
        RouteFilter update = new RouteFilter();
        update.setId(filter.getId());
        update.setRouteFilterKey(edit.getRouteFilterKey());
        update.setRouteFilterValue(edit.getRouteFilterValue());
        update.setRouteFilterComment(edit.getRouteFilterComment());
        extRouteFilterMapper.update(update);
    }

    @Override
    public void routeFilterAdd(RouteFilterEdit edit) throws BusinessException {
        RouteInfo routeInfo = extRouteInfoMapper.get(edit.getRouteMark());
        if (routeInfo == null)
            throw new BusinessException("未找到路由信息");
        RouteFilter insert = new RouteFilter();
        insert.setRouteId(routeInfo.getId());
        insert.setRouteFilterKey(edit.getRouteFilterKey());
        insert.setRouteFilterValue(edit.getRouteFilterValue());
        insert.setRouteFilterComment(edit.getRouteFilterComment());
        extRouteFilterMapper.insert(insert);
    }

    @Override
    public void routeFilterDel(RouteFilterDel del) throws BusinessException {
        RouteFilter filter = extRouteFilterMapper.get(Long.valueOf(del.getId()));
        if (filter == null)
            throw new BusinessException("未找到过滤器信息");
        extRouteFilterMapper.delete(filter.getId(), del.getRouteFilterKey());
    }
}
