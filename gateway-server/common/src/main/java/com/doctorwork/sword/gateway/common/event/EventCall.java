package com.doctorwork.sword.gateway.common.event;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:42 2019/7/15
 * @Modified By:
 */
public interface EventCall<T> {
    //no Exception to throw
    T call();
}
