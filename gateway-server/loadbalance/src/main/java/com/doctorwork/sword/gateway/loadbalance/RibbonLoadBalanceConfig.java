package com.doctorwork.sword.gateway.loadbalance;

import com.doctorwork.sword.gateway.common.Constants;
import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.loadbalance.param.ext.LoadbalanceParam;
import com.doctorwork.sword.gateway.loadbalance.param.ext.RibbonLoadBalanceParam;
import com.doctorwork.sword.gateway.loadbalance.param.PingParam;
import com.doctorwork.sword.gateway.loadbalance.param.ping.RibbonPingParam;
import com.doctorwork.sword.gateway.loadbalance.param.rule.RuleParam;
import com.doctorwork.sword.gateway.loadbalance.param.rule.WeightRule;
import com.netflix.loadbalancer.*;
import org.springframework.util.StringUtils;

/**
 * @author chenzhiqiang
 * @date 2019/6/21
 */
public class RibbonLoadBalanceConfig {
    private final RibbonPingParam<IPing> pingParam;

    private final LoadbalanceParam loadbalanceParam;

    private final RuleParam ruleParam;

    private final LoadbalanceInfo loadbalanceInfo;

    public RibbonLoadBalanceConfig(LoadbalanceInfo loadbalanceInfo) {
        this.loadbalanceInfo = loadbalanceInfo;
        this.pingParam = PingParam.build(loadbalanceInfo);
        this.loadbalanceParam = LoadbalanceParam.build(loadbalanceInfo);
        this.ruleParam = RuleParam.build(loadbalanceInfo);
    }

    public IPing ping() {
        if (pingParam == null)
            return null;
        return pingParam.ping();
    }

    public RibbonPingParam pingParam() {
        return pingParam;
    }

    public RibbonLoadBalanceParam extParam() {
        return loadbalanceParam instanceof RibbonLoadBalanceParam ? null : (RibbonLoadBalanceParam) loadbalanceParam;
    }

    public IRule rule() {
        String rule = this.ruleParam.getLbRule();
        if (StringUtils.isEmpty(rule))
            return null;
        if (Constants.LBRULE_RANDOM.equalsIgnoreCase(rule)) {
            return new RandomRule();
        } else if (Constants.LBRULE_ROUNDROBIN.equalsIgnoreCase(rule)) {
            return new RoundRobinRule();
        } else if (Constants.LBRULE_RETRY.equalsIgnoreCase(rule)) {
            return new RetryRule();
        } else if (Constants.LBRULE_AVAILABILITYFILTERING.equalsIgnoreCase(rule)) {
            return new AvailabilityFilteringRule();
        } else if (Constants.LBRULE_BESTAVAILABLE.equalsIgnoreCase(rule)) {
            return new BestAvailableRule();
        } else if (Constants.LBRULE_WEIGHTEDRESPONSETIME.equalsIgnoreCase(rule)) {
            return new WeightedResponseTimeRule();
        } else if (Constants.LBRULE_ZONEAVOIDANCE.equalsIgnoreCase(rule)) {
            return new ZoneAvoidanceRule();
        } else if (Constants.LBRULE_WEIGHTROUNDROBIN.equalsIgnoreCase(rule)) {
            return new WeightRule();
        }
        return null;
    }

    public LoadbalanceInfo getLoadbalanceInfo() {
        return loadbalanceInfo;
    }
}
