package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:45 2019/7/10
 * @Modified By:
 */
public interface ILoadBalancerConfigRepository {
    LoadbalanceInfo loadbalanceConfig(String lbMark);
}
