package com.doctorwork.sword.gateway.loadbalance;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:02 2019/7/5
 * @Modified By:
 */
public interface ILoadBalanceClientManagerApi {
    void loadBalanceInit(String lbMark);

    void loadBalanceLoad(String lbMark);

    void loadBalanceDelete(String lbMark);

    void loadBalancePingLoad(String lbMark);

    void loadBalanceRuleLoad(String lbMark);

    void loadBalanceAutoRefreshLoad(String lbMark);

    void loadBalanceAutoRefreshShutdown(String lbMark);

    void loadBalanceDiscoveryLoad(String lbMark);
}
