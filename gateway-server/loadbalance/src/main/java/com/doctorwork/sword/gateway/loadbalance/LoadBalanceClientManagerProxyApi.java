package com.doctorwork.sword.gateway.loadbalance;

/**
 * @author chenzhiqiang
 * @date 2019/7/6
 */
public class LoadBalanceClientManagerProxyApi implements ILoadBalanceClientManagerApi {

    private ILoadBalanceClientManagerApi loadBalanceClientManagerApi;

    @Override
    public void loadBalanceInit(String lbMark) {
        loadBalanceClientManagerApi.loadBalanceInit(lbMark);
    }

    @Override
    public void loadBalanceDelete(String lbMark) {
        loadBalanceClientManagerApi.loadBalanceDelete(lbMark);
    }

    @Override
    public void loadBalancePingLoad(String lbMark) {
        loadBalanceClientManagerApi.loadBalancePingLoad(lbMark);
    }

    @Override
    public void loadBalanceRuleLoad(String lbMark) {
        loadBalanceClientManagerApi.loadBalanceRuleLoad(lbMark);
    }

    @Override
    public void loadBalanceAutoRefreshLoad(String lbMark) {
        loadBalanceClientManagerApi.loadBalanceAutoRefreshLoad(lbMark);
    }

    @Override
    public void loadBalanceAutoRefreshShutdown(String lbMark) {
        loadBalanceClientManagerApi.loadBalanceAutoRefreshShutdown(lbMark);
    }

    @Override
    public void loadBalanceDiscoveryLoad(String lbMark) {
        loadBalanceClientManagerApi.loadBalanceDiscoveryLoad(lbMark);
    }

    public void setLoadBalanceClientManagerApi(ILoadBalanceClientManagerApi loadBalanceClientManagerApi) {
        this.loadBalanceClientManagerApi = loadBalanceClientManagerApi;
    }
}
