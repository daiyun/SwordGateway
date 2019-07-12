package com.doctorwork.sword.gateway.loadbalance.param.rule;

import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.common.config.LoadBalancerInfo;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.doctorwork.sword.gateway.loadbalance.param.Param;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author chenzhiqiang
 * @date 2019/6/22
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "lbRule", include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class RuleParam implements Param {
    private String lbRule;

    public static RuleParam build(LoadBalancerInfo loadbalanceInfo) {
        if (!StringUtils.isEmpty(loadbalanceInfo.getRuleParam())) {
            return JacksonUtil.toSubTypeObject(loadbalanceInfo.getRuleParam(), RuleParam.class);
        }
        return null;
    }

    public RuleParam(String lbRule) {
        this.lbRule = lbRule;
    }

    public String getLbRule() {
        return lbRule;
    }

    public void setLbRule(String lbRule) {
        this.lbRule = lbRule;
    }
}
