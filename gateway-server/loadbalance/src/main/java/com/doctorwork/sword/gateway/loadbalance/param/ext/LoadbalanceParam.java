package com.doctorwork.sword.gateway.loadbalance.param.ext;

import com.doctorwork.sword.gateway.common.Constants;
import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.common.config.LoadBalancerInfo;
import com.doctorwork.sword.gateway.loadbalance.param.Param;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author chenzhiqiang
 * @date 2019/6/22
 */
public abstract class LoadbalanceParam<T> implements Param {

    public static LoadbalanceParam build(LoadBalancerInfo loadbalanceInfo) {
        if (Constants.LBTYPE_RIBBON.equals(loadbalanceInfo.getType())) {
            return JacksonUtil.toObject(loadbalanceInfo.getLbExtParam(), RibbonLoadBalanceParam.class);
        }
        return null;
    }
}
