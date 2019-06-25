package com.doctorwork.sword.gateway.loadbalance.param;

import com.doctorwork.sword.gateway.common.Regulation;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author chenzhiqiang
 * @date 2019/6/21
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface Param extends Regulation {
}
