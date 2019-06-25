package com.doctorwork.sword.gateway.loadbalance.param.ping;

import com.doctorwork.sword.gateway.common.Constants;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.netflix.loadbalancer.PingConstant;
import org.springframework.util.StringUtils;

/**
 * @author chenzhiqiang
 * @date 2019/6/21
 */
@JsonTypeName(Constants.PINGMODE_CONSTANT)
public class ConstantPingParam extends RibbonPingParam<PingConstant> {
    private String constant;

    public ConstantPingParam() {
        super(Constants.PINGMODE_CONSTANT, null);
    }

    @Override
    public PingConstant ping() {
        PingConstant pingConstant = new PingConstant();
        if (StringUtils.isEmpty(constant))
            return pingConstant;
        pingConstant.setConstant(constant);
        return pingConstant;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }
}
