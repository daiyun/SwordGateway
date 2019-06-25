package com.doctorwork.sword.gateway.loadbalance.param.ping;

import com.doctorwork.sword.gateway.common.Constants;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.netflix.loadbalancer.NoOpPing;

/**
 * @author chenzhiqiang
 * @date 2019/6/21
 */
@JsonTypeName(Constants.PINGMODE_NOOP)
public class NoOPPingParam extends RibbonPingParam<NoOpPing> {
    public NoOPPingParam() {
        super(Constants.PINGMODE_NOOP, null);
    }

    @Override
    public NoOpPing ping() {
        return new NoOpPing();
    }
}
