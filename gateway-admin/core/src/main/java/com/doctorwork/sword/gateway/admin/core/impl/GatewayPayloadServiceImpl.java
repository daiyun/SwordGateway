package com.doctorwork.sword.gateway.admin.core.impl;

import com.doctorwork.doctorwork.admin.api.req.PayloadDel;
import com.doctorwork.doctorwork.admin.api.req.PayloadEdit;
import com.doctorwork.doctorwork.admin.api.req.PayloadSearchReq;
import com.doctorwork.doctorwork.admin.api.res.PayloadInfoRes;
import com.doctorwork.doctorwork.admin.api.res.PayloadRes;
import com.doctorwork.sword.gateway.admin.core.GatewayPayloadService;
import com.doctorwork.sword.gateway.admin.dal.mapper.LoadbalanceInfoMapper;
import com.doctorwork.sword.gateway.admin.dal.mapper.ext.ExtLoadbalanceInfoMapper;
import com.doctorwork.sword.gateway.admin.dal.model.LoadbalanceInfo;
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
 * @Date: 14:16 2019/7/26
 * @Modified By:
 */
@Service
public class GatewayPayloadServiceImpl implements GatewayPayloadService {

    @Autowired
    private ExtLoadbalanceInfoMapper extLoadbalanceInfoMapper;

    @Autowired
    private LoadbalanceInfoMapper loadbalanceInfoMapper;

    @Override
    public PageResult<PayloadRes> payloadSearch(PayloadSearchReq req) {
        PageHelper.offsetPage(req.getPageNum(), req.getPageSize());
        Page<LoadbalanceInfo> page = (Page<LoadbalanceInfo>) extLoadbalanceInfoMapper.list();
        List<PayloadRes> resList = page.getResult().stream().map(routeInfo -> {
            PayloadRes res = new PayloadRes();
            res.setLbMark(routeInfo.getLbMark());
            res.setDscrEnable(routeInfo.getDscrEnable());
            res.setDscrId(routeInfo.getDscrId());
            res.setLbComment(routeInfo.getLbComment());
            res.setLbName(routeInfo.getLbName());
            res.setLbType(routeInfo.getLbType());
            res.setLbStatus(routeInfo.getLbStatus());
            return res;
        }).collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), page.getPages(), page.getPageNum(), page.getPageSize(), resList);
    }

    @Override
    public PayloadInfoRes payloadGet(String lbMark) throws BusinessException {
        LoadbalanceInfo loadbalanceInfo = extLoadbalanceInfoMapper.get(lbMark);
        if (loadbalanceInfo == null)
            throw new BusinessException("未找到负载器信息");
        PayloadInfoRes res = new PayloadInfoRes();
        res.setLbMark(loadbalanceInfo.getLbMark());
        res.setLbName(loadbalanceInfo.getLbName());
        res.setDscrEnable(loadbalanceInfo.getDscrEnable());
        res.setDscrId(loadbalanceInfo.getDscrId());
        res.setLbExtParam(loadbalanceInfo.getLbExtParam());
        res.setLbComment(loadbalanceInfo.getLbComment());
        res.setPingParam(loadbalanceInfo.getPingParam());
        res.setRuleParam(loadbalanceInfo.getRuleParam());
        res.setLbType(loadbalanceInfo.getLbType());
        res.setLbStatus(loadbalanceInfo.getLbStatus());
        return res;
    }

    @Override
    public void payloadUpdate(PayloadEdit edit) throws BusinessException {
        LoadbalanceInfo loadbalanceInfo = extLoadbalanceInfoMapper.get(edit.getLbMark());
        if (loadbalanceInfo == null)
            throw new BusinessException("未找到负载器信息");
        LoadbalanceInfo update = new LoadbalanceInfo();
        update.setLbMark(edit.getLbMark());
        update.setLbComment(edit.getLbComment());
        update.setLbName(edit.getLbName());
        update.setLbType(edit.getLbType());
        update.setDscrEnable(edit.getDscrEnable());
        update.setDscrId(edit.getDscrId());
        update.setPingParam(edit.getPingParam());
        update.setLbExtParam(edit.getLbExtParam());
        update.setRuleParam(edit.getRuleParam());
        extLoadbalanceInfoMapper.update(loadbalanceInfo);
    }

    @Override
    public void payloadAdd(PayloadEdit edit) throws BusinessException {
        LoadbalanceInfo loadbalanceInfo = extLoadbalanceInfoMapper.get(edit.getLbMark());
        if (loadbalanceInfo == null)
            throw new BusinessException("负载器信息已存在");
        LoadbalanceInfo insert = new LoadbalanceInfo();
        insert.setLbMark(edit.getLbMark());
        insert.setLbComment(edit.getLbComment());
        insert.setLbName(edit.getLbName());
        insert.setLbType(edit.getLbType());
        insert.setDscrEnable(edit.getDscrEnable());
        insert.setDscrId(edit.getDscrId());
        insert.setPingParam(edit.getPingParam());
        insert.setLbExtParam(edit.getLbExtParam());
        insert.setRuleParam(edit.getRuleParam());
        insert.setLbStatus(0);
        loadbalanceInfoMapper.insert(insert);
    }

    @Override
    public void payloadDel(PayloadDel del) throws BusinessException {
        LoadbalanceInfo loadbalanceInfo = extLoadbalanceInfoMapper.get(del.getLbMark());
        if (loadbalanceInfo == null)
            throw new BusinessException("未找到负载器信息");
        //发布配置
        extLoadbalanceInfoMapper.delete(del.getLbMark());
    }

    @Override
    public void payloadEnable(String lbMark) throws BusinessException {
        LoadbalanceInfo loadbalanceInfo = extLoadbalanceInfoMapper.get(lbMark);
        if (loadbalanceInfo == null)
            throw new BusinessException("未找到负载器信息");
        //发布配置
        extLoadbalanceInfoMapper.updateStatus(lbMark, 1);
    }

    @Override
    public void payloadDisable(String lbMark) throws BusinessException {
        LoadbalanceInfo loadbalanceInfo = extLoadbalanceInfoMapper.get(lbMark);
        if (loadbalanceInfo == null)
            throw new BusinessException("未找到负载器信息");
        //发布配置
        extLoadbalanceInfoMapper.updateStatus(lbMark, 2);
    }
}