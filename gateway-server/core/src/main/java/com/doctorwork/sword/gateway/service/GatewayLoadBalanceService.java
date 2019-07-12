package com.doctorwork.sword.gateway.service;

import com.doctorwork.sword.gateway.common.config.LoadBalancerInfo;
import com.doctorwork.sword.gateway.common.config.LoadBalancerServer;

import java.util.List;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:17 2019/6/21
 * @Modified By:
 */
public interface GatewayLoadBalanceService {

    List<LoadBalancerServer> loadBalanceServers(String lbMark);

    LoadBalancerInfo loadBalance(String lbMark);
}
