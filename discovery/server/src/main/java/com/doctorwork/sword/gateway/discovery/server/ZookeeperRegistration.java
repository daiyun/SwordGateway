package com.doctorwork.sword.gateway.discovery.server;

import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import org.apache.curator.x.discovery.ServiceInstance;

/**
 * @Author:czq
 * @Description:
 * @Date: 9:55 2019/5/31
 * @Modified By:
 */
public interface ZookeeperRegistration extends Registration {

    ServiceInstance<ZookeeperInstance> getServiceInstance();

}
