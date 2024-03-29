package com.doctorwork.sword.gateway.loadbalance.param;

import com.doctorwork.sword.gateway.common.Constants;
import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.common.config.LoadBalancerInfo;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.doctorwork.sword.gateway.loadbalance.param.ping.RibbonPingParam;

/**
 * @author chenzhiqiang
 * @date 2019/6/21
 */

public abstract class PingParam<T> implements Param {
    public static RibbonPingParam build(LoadBalancerInfo loadbalanceInfo) {
        if(StringUtils.isEmpty(loadbalanceInfo.getPingParam())){
            return null;
        }
        if (Constants.LBTYPE_RIBBON.equals(loadbalanceInfo.getType())) {
            return JacksonUtil.toSubTypeObject(loadbalanceInfo.getPingParam(), RibbonPingParam.class);
        }
        return null;
    }

    public abstract T ping();
}
