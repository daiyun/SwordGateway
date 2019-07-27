package com.doctorwork.sword.gateway.admin.core.dto;

import com.doctorwork.doctorwork.admin.api.req.PayloadRule;
import com.doctorwork.sword.gateway.common.Serialize;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;

/**
 * @author chenzhiqiang
 * @date 2019/7/27
 */
public class RuleParam implements Serialize {
    private String lbRule;

    private RuleParam() {
    }

    public String getLbRule() {
        return lbRule;
    }

    public void setLbRule(String lbRule) {
        this.lbRule = lbRule;
    }

    public static RuleParam param(PayloadRule payloadRule) {
        if (payloadRule == null || StringUtils.isEmpty(payloadRule.getLbRule()))
            return null;
        RuleParam ruleParam = new RuleParam();
        ruleParam.setLbRule(payloadRule.getLbRule());
        return ruleParam;
    }
}
