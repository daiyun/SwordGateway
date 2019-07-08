package com.doctorwork.sword.gateway;

import com.doctorwork.sword.gateway.loadbalance.ILoadBalanceClientManagerApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenzhiqiang
 * @date 2019/7/4
 */
@RequestMapping("/lb")
@RestController
public class LoadBalanceController {

    @Autowired
    private ILoadBalanceClientManagerApi loadBalanceClientManagerApi;

    @RequestMapping("init")
    @ResponseBody
    public void loadBalanceInit(String lbmark) {
        loadBalanceClientManagerApi.loadBalanceInit(lbmark);
    }

    @RequestMapping("load")
    @ResponseBody
    public void loadBalanceLoad(String lbmark) {
        loadBalanceClientManagerApi.loadBalanceLoad(lbmark);
    }

    @RequestMapping("del")
    @ResponseBody
    public void loadBalanceDelete(String lbmark) {
        loadBalanceClientManagerApi.loadBalanceDelete(lbmark);
    }

    @RequestMapping("autorefresh/load")
    @ResponseBody
    public void loadBalanceAutoRefreshLoad(String lbmark) {
        loadBalanceClientManagerApi.loadBalanceAutoRefreshLoad(lbmark);
    }

    @RequestMapping("autorefresh/shutdown")
    @ResponseBody
    public void loadBalanceAutoRefreshShutdown(String lbmark) {
        loadBalanceClientManagerApi.loadBalanceAutoRefreshShutdown(lbmark);
    }

    @RequestMapping("ping/load")
    @ResponseBody
    public void loadBalancePingLoad(String lbmark) {
        loadBalanceClientManagerApi.loadBalancePingLoad(lbmark);
    }

    @RequestMapping("rule/load")
    @ResponseBody
    public void loadBalanceRuleLoad(String lbmark) {
        loadBalanceClientManagerApi.loadBalanceRuleLoad(lbmark);
    }

    @RequestMapping("discover/load")
    @ResponseBody
    public void loadBalanceDiscoveryLoad(String lbmark) {
        loadBalanceClientManagerApi.loadBalanceDiscoveryLoad(lbmark);
    }
}
