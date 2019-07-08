package com.doctorwork.sword.gateway.discovery.connection;

import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.Collection;

/**
 * @Author:czq
 * @Description:
 * @Date: 15:05 2019/7/8
 * @Modified By:
 */
public interface IQueryServiceInstancecs {
    Collection<ServiceInstance<ZookeeperInstance>> getInstances(String name) throws Exception;
}
