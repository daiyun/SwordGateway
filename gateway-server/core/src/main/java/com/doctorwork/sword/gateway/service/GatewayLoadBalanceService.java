package com.doctorwork.sword.gateway.service;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.dal.model.LoadbalanceServer;

import java.util.List;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:17 2019/6/21
 * @Modified By:
 */
public interface GatewayLoadBalanceService {

    List<LoadbalanceServer> loadBalanceServers(String lbMark);

    LoadbalanceInfo loadBalance(String lbMark);
}
