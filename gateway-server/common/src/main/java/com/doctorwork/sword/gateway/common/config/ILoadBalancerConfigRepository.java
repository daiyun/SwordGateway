package com.doctorwork.sword.gateway.common.config;

import java.util.Collection;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:45 2019/7/10
 * @Modified By:
 */
public interface ILoadBalancerConfigRepository {
    LoadBalancerInfo loadbalanceConfig(String lbMark);

    Collection<LoadBalancerServer> loadbalanceServer(String lbMark);
}
