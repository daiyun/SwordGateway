package com.doctorwork.sword.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:46 2019/7/10
 * @Modified By:
 */
public class AbstractConfiguration implements IDiscoveryConfigRepository, IConnectionConfigRepository, ILoadBalancerConfigRepository {
    protected static Logger logger = LoggerFactory.getLogger(AbstractConfiguration.class);
}
