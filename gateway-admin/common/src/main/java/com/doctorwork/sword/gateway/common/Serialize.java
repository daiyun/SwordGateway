package com.doctorwork.sword.gateway.common;

/**
 * @author chenzhiqiang
 * @date 2019/6/22
 */
public interface Serialize {
    default String serialize() {
        return JacksonUtil.toJSon(this);
    }
}
