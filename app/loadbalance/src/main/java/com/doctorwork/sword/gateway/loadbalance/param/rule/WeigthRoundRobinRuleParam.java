package com.doctorwork.sword.gateway.loadbalance.param.rule;

import com.doctorwork.sword.gateway.common.Constants;
import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName(Constants.LBRULE_WEIGHTROUNDROBIN)
public class WeigthRoundRobinRuleParam extends RuleParam {
    public WeigthRoundRobinRuleParam() {
        super(Constants.LBRULE_WEIGHTROUNDROBIN);
    }

    public static void main(String[] args) {
        System.out.println(JacksonUtil.toJSon(new WeigthRoundRobinRuleParam()));
    }
}
