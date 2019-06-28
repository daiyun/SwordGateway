package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.loadbalance.DataBaseServerList;
import com.doctorwork.sword.gateway.loadbalance.DatabaseLoadBalancer;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:28 2019/6/18
 * @Modified By:
 */
public class LoadBalancerConfiguration {

//    @Autowired
//    private GatewayLoadBalanceService gatewayLoadBalanceService;
//
//    @Bean
//    @ConditionalOnMissingBean
//    public ILoadBalancer loadBalancer(IClientConfig config) {
//        LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(config.getClientName());
//        if (loadbalanceInfo == null)
//            throw new RuntimeException("no available loadbalance for " + config.getClientName());
//        DataBaseServerList serverList = new DataBaseServerList(gatewayLoadBalanceService);
//        serverList.initFromDependencies(config);
//        return new DatabaseLoadBalancer(serverList, config, loadbalanceInfo);
//    }
}
