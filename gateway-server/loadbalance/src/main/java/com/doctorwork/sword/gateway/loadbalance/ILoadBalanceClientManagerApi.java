package com.doctorwork.sword.gateway.loadbalance;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:02 2019/7/5
 * @Modified By:
 */
public interface ILoadBalanceClientManagerApi {
    void loadBalanceInit(String serviceId);

    void loadBalanceDelete(String serviceId);

    void loadBalancePingLoad(String serviceId);

    void loadBalanceRuleLoad(String serviceId);

    void loadBalanceDiscoveryLoad(String serviceId);
}
