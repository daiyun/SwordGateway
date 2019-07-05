package com.doctorwork.sword.gateway.loadbalance;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:02 2019/7/5
 * @Modified By:
 */
public interface ILoadBalanceClientManagerApi {
    void start(String serviceId);

    void shutdown(String serviceId);
}
