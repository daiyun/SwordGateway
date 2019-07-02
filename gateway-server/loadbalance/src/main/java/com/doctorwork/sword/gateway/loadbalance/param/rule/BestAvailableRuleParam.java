package com.doctorwork.sword.gateway.loadbalance.param.rule;

import com.doctorwork.sword.gateway.common.Constants;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author chenzhiqiang
 * @date 2019/6/22
 */
@JsonTypeName(Constants.LBRULE_BESTAVAILABLE)
public class BestAvailableRuleParam extends RuleParam {
    public BestAvailableRuleParam() {
        super(Constants.LBRULE_BESTAVAILABLE);
    }
}
