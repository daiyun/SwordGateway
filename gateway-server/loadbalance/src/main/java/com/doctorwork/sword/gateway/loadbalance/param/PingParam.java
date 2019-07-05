package com.doctorwork.sword.gateway.loadbalance.param;

import com.doctorwork.sword.gateway.common.Constants;
import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.doctorwork.sword.gateway.loadbalance.param.ping.RibbonPingParam;

/**
 * @author chenzhiqiang
 * @date 2019/6/21
 */

public abstract class PingParam<T> implements Param {
    private String lbType;

    public static RibbonPingParam build(LoadbalanceInfo loadbalanceInfo) {
        if(StringUtils.isEmpty(loadbalanceInfo.getPingParam())){
            return null;
        }
        if (Constants.LBTYPE_RIBBON.equals(loadbalanceInfo.getLbType())) {
            return JacksonUtil.toSubTypeObject(loadbalanceInfo.getPingParam(), RibbonPingParam.class);
        }
        return null;
    }

    public abstract T ping();

    public PingParam(String lbType) {
        this.lbType = lbType;
    }

    public String getLbType() {
        return lbType;
    }

    public void setLbType(String lbType) {
        this.lbType = lbType;
    }
}
