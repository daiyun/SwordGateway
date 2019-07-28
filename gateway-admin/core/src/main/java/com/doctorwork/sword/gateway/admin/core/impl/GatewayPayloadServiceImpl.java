package com.doctorwork.sword.gateway.admin.core.impl;

import com.doctorwork.doctorwork.admin.api.req.*;
import com.doctorwork.doctorwork.admin.api.res.PayloadDiscoverServerRes;
import com.doctorwork.doctorwork.admin.api.res.PayloadInfoRes;
import com.doctorwork.doctorwork.admin.api.res.PayloadRes;
import com.doctorwork.doctorwork.admin.api.res.PayloadServerRes;
import com.doctorwork.sword.gateway.admin.core.GatewayPayloadService;
import com.doctorwork.sword.gateway.admin.core.dto.ExtParam;
import com.doctorwork.sword.gateway.admin.core.dto.PingParam;
import com.doctorwork.sword.gateway.admin.core.dto.RuleParam;
import com.doctorwork.sword.gateway.admin.dal.mapper.LoadbalanceInfoMapper;
import com.doctorwork.sword.gateway.admin.dal.mapper.LoadbalanceServerMapper;
import com.doctorwork.sword.gateway.admin.dal.mapper.ext.ExtDiscoverConfigMapper;
import com.doctorwork.sword.gateway.admin.dal.mapper.ext.ExtLoadbalanceInfoMapper;
import com.doctorwork.sword.gateway.admin.dal.mapper.ext.ExtLoadbalanceServerMapper;
import com.doctorwork.sword.gateway.admin.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.admin.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.admin.dal.model.LoadbalanceServer;
import com.doctorwork.sword.gateway.common.BusinessException;
import com.doctorwork.sword.gateway.common.Constant;
import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.common.PageResult;
import com.doctorwork.sword.gateway.discovery.common.DiscoveryProperties;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
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

    @Autowired
    private ExtDiscoverConfigMapper extDiscoverConfigMapper;

    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    private ExtLoadbalanceServerMapper extLoadbalanceServerMapper;

    @Autowired
    private LoadbalanceServerMapper loadbalanceServerMapper;

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
        res.setLbComment(loadbalanceInfo.getLbComment());
        res.setLbType(loadbalanceInfo.getLbType());
        res.setLbStatus(loadbalanceInfo.getLbStatus());
        res.setPayloadPing(JacksonUtil.toObject(loadbalanceInfo.getPingParam(), PayloadPing.class));
        res.setPayloadRule(JacksonUtil.toObject(loadbalanceInfo.getRuleParam(), PayloadRule.class));
        res.setServerReload(JacksonUtil.toObject(loadbalanceInfo.getLbExtParam(), PayloadServerReload.class));
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
        PingParam pingParam = PingParam.param(edit.getPayloadPing());
        if (pingParam != null)
            update.setPingParam(pingParam.serialize());
        RuleParam ruleParam = RuleParam.param(edit.getPayloadRule());
        if (ruleParam != null)
            update.setRuleParam(ruleParam.serialize());
        ExtParam extParam = ExtParam.param(edit.getPayloadServerReload());
        if (extParam != null)
            update.setLbExtParam(extParam.serialize());
        extLoadbalanceInfoMapper.update(update);
    }

    @Override
    public void payloadAdd(PayloadEdit edit) throws BusinessException {
        LoadbalanceInfo loadbalanceInfo = extLoadbalanceInfoMapper.get(edit.getLbMark());
        if (loadbalanceInfo != null)
            throw new BusinessException("负载器信息已存在");
        LoadbalanceInfo insert = new LoadbalanceInfo();
        insert.setLbMark(edit.getLbMark());
        insert.setLbComment(edit.getLbComment());
        insert.setLbName(edit.getLbName());
        insert.setLbType(edit.getLbType());
        insert.setDscrEnable(edit.getDscrEnable());
        insert.setDscrId(edit.getDscrId());
        PingParam pingParam = PingParam.param(edit.getPayloadPing());
        if (pingParam != null)
            insert.setPingParam(pingParam.serialize());
        RuleParam ruleParam = RuleParam.param(edit.getPayloadRule());
        if (ruleParam != null)
            insert.setRuleParam(ruleParam.serialize());
        ExtParam extParam = ExtParam.param(edit.getPayloadServerReload());
        if (extParam != null)
            insert.setLbExtParam(extParam.serialize());
        insert.setLbStatus(0);//
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

    @Override
    public PageResult<PayloadServerRes> payloadServerList(PayloadServerSearchReq req) throws BusinessException {
        LoadbalanceInfo loadbalanceInfo = extLoadbalanceInfoMapper.get(req.getLbMark());
        if (loadbalanceInfo == null)
            throw new BusinessException("未找到负载器信息");
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        Page<LoadbalanceServer> page = (Page<LoadbalanceServer>) extLoadbalanceServerMapper.getByLbMark(req.getLbMark());
        return new PageResult<>(page.getTotal(), page.getPages(), req.getPageNum(), req.getPageSize(), page.stream().map(loadbalanceServer -> {
            PayloadServerRes res = new PayloadServerRes();
            res.setId(String.valueOf(loadbalanceServer.getId()));
            res.setComment(loadbalanceServer.getComment());
            res.setLbMark(loadbalanceServer.getLbMark());
            res.setSrvIp(loadbalanceServer.getSrvIp());
            res.setSrvPort(loadbalanceServer.getSrvPort());
            res.setSrvName(loadbalanceServer.getSrvName());
            res.setSrvWeight(loadbalanceServer.getSrvWeight());
            res.setSrvStatus(loadbalanceServer.getSrvStatus());
            res.setSrvEnable(loadbalanceServer.getSrvEnable());
            return res;
        }).collect(Collectors.toList()));
    }

    @Override
    public void payloadServerAdd(PayloadServerEdit edit) throws BusinessException {
        LoadbalanceServer exits = extLoadbalanceServerMapper.getByIpPort(edit.getLbMark(), edit.getSrvIp(), edit.getSrvPort());
        if (exits != null)
            throw new BusinessException("已存在该服务");
        LoadbalanceServer insert = new LoadbalanceServer();
        insert.setLbMark(edit.getLbMark());
        insert.setComment(edit.getComment());
        insert.setSrvIp(edit.getSrvIp());
        insert.setSrvPort(edit.getSrvPort());
        insert.setSrvWeight(edit.getSrvWeight());
        insert.setSrvName(edit.getSrvName());
        insert.setSrvStatus(0);
        insert.setSrvEnable(1);
        insert.setSrvId(edit.getSrvIp().concat(":").concat(String.valueOf(edit.getSrvPort())));
        loadbalanceServerMapper.insert(insert);
    }

    @Override
    public void payloadServerUpdate(PayloadServerEdit edit) throws BusinessException {
        LoadbalanceServer exits = extLoadbalanceServerMapper.get(Long.valueOf(edit.getId()));
        if (exits == null)
            throw new BusinessException("不存在该服务");
        LoadbalanceServer update = new LoadbalanceServer();
        update.setId(Long.valueOf(edit.getId()));
        update.setLbMark(edit.getLbMark());
        update.setComment(edit.getComment());
        update.setSrvIp(edit.getSrvIp());
        update.setSrvPort(edit.getSrvPort());
        update.setSrvWeight(edit.getSrvWeight());
        update.setSrvName(edit.getSrvName());
        update.setSrvId(edit.getSrvIp().concat(":").concat(String.valueOf(edit.getSrvPort())));
        extLoadbalanceServerMapper.update(update);
    }

    @Override
    public void payloadServerDel(String id) throws BusinessException {
        LoadbalanceServer exits = extLoadbalanceServerMapper.get(Long.valueOf(id));
        if (exits == null)
            throw new BusinessException("不存在该服务");
        extLoadbalanceServerMapper.delete(Long.valueOf(id));
    }

    @Override
    public void payloadServerOn(String id) throws BusinessException {
        Long tmp = Long.valueOf(id);
        LoadbalanceServer exits = extLoadbalanceServerMapper.get(tmp);
        if (exits == null)
            throw new BusinessException("不存在该服务");
        extLoadbalanceServerMapper.updateStatus(tmp, 1);
    }

    @Override
    public void payloadServerOff(String id) throws BusinessException {
        Long tmp = Long.valueOf(id);
        LoadbalanceServer exits = extLoadbalanceServerMapper.get(tmp);
        if (exits == null)
            throw new BusinessException("不存在该服务");
        extLoadbalanceServerMapper.updateStatus(tmp, 0);
    }

    @Override
    public void payloadServerEnable(String id) throws BusinessException {
        Long tmp = Long.valueOf(id);
        LoadbalanceServer exits = extLoadbalanceServerMapper.get(tmp);
        if (exits == null)
            throw new BusinessException("不存在该服务");
        extLoadbalanceServerMapper.updateEnable(tmp, 0);
    }

    @Override
    public void payloadServerDisable(String id) throws BusinessException {
        Long tmp = Long.valueOf(id);
        LoadbalanceServer exits = extLoadbalanceServerMapper.get(tmp);
        if (exits == null)
            throw new BusinessException("不存在该服务");
        extLoadbalanceServerMapper.updateEnable(tmp, 1);
    }

    @Override
    public PageResult<PayloadDiscoverServerRes> payloadDiscoverServerList(PayloadServerSearchReq req) throws Exception {
        LoadbalanceInfo loadbalanceInfo = extLoadbalanceInfoMapper.get(req.getLbMark());
        if (loadbalanceInfo == null)
            throw new BusinessException("未找到负载器信息");
        if (!Objects.equals(1, loadbalanceInfo.getDscrEnable()))
            return new PageResult<PayloadDiscoverServerRes>(0, 0, req.getPageNum(), req.getPageSize(), Collections.emptyList());
        DiscoverConfig discoverConfig = extDiscoverConfigMapper.get(loadbalanceInfo.getDscrId());
        if (discoverConfig == null)
            throw new BusinessException("未获取服务发现配置");
        DiscoveryProperties properties = JacksonUtil.toObject(discoverConfig.getDscrConfig(), DiscoveryProperties.class);
        String path = properties.getZkRoot().concat("/").concat(req.getLbMark()).concat(Constant.PROVIDES);
        List<String> list = curatorFramework.getChildren().forPath(path);
        List<String> nodes = new ArrayList<>(req.getPageSize());
        int count = 0;
        ListIterator<String> iterator = list.listIterator((req.getPageNum() - 1) * req.getPageSize());
        while (iterator.hasNext() && count < req.getPageSize()) {
            nodes.add(iterator.next());
            count++;
        }
        if (CollectionUtils.isEmpty(nodes))
            new PageResult<PayloadDiscoverServerRes>(0, 0, req.getPageNum(), req.getPageSize(), Collections.emptyList());
        List<PayloadDiscoverServerRes> resList = new ArrayList<>(nodes.size());
        for (String node : nodes) {
            byte[] nodeBytes = curatorFramework.getData().forPath(path.concat("/").concat(node));
            ServiceInstance<ZookeeperInstance> nodeInstance = JacksonUtil.toObject(nodeBytes, ServiceInstance.class);
            PayloadDiscoverServerRes res = new PayloadDiscoverServerRes();
            res.setId(nodeInstance.getId());
            res.setLbMark(req.getLbMark());
            res.setSrvIp(nodeInstance.getAddress());
            res.setSrvPort(nodeInstance.getPort());
            res.setMetaData(nodeInstance.getPayload().getMetadata());
            resList.add(res);
        }
        return new PageResult<>(list.size(), list.size() % req.getPageSize(), req.getPageNum(), req.getPageSize(), resList);
    }
}
