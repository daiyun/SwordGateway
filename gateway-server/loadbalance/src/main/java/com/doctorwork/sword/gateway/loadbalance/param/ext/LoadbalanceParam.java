package com.doctorwork.sword.gateway.loadbalance.param.ext;

import com.doctorwork.sword.gateway.common.Constants;
import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.loadbalance.param.Param;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author chenzhiqiang
 * @date 2019/6/22
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "lbType", include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class LoadbalanceParam<T> implements Param {
    private String lbType;

    public static LoadbalanceParam build(LoadbalanceInfo loadbalanceInfo) {
        if (Constants.LBTYPE_RIBBON.equals(loadbalanceInfo.getLbType())) {
            return JacksonUtil.toSubTypeObject(loadbalanceInfo.getLbExtParam(), RibbonLoadBalanceParam.class);
        }
        return null;
    }

    public LoadbalanceParam(String lbType) {
        this.lbType = lbType;
    }

    public String getLbType() {
        return lbType;
    }

    public void setLbType(String lbType) {
        this.lbType = lbType;
    }
}
