package com.doctorwork.sword.gateway.loadbalance.server;

import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import org.apache.curator.x.discovery.ServiceInstance;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:29 2019/7/2
 * @Modified By:
 */
public class ZookeeperServer extends AbstractServer {
    private final ServiceInstance<ZookeeperInstance> serviceInstance;

    public ZookeeperServer(ServiceInstance<ZookeeperInstance> serviceInstance) {
        // TODO: ssl support
        super(serviceInstance.getAddress(), serviceInstance.getPort(), new MetaInfo() {
            @Override
            public String getAppName() {
                return serviceInstance.getName();
            }

            @Override
            public String getServerGroup() {
                return null;
            }

            @Override
            public String getServiceIdForDiscovery() {
                return serviceInstance.getName();
            }

            @Override
            public String getInstanceId() {
                return serviceInstance.getId();
            }
        });
        this.serviceInstance = serviceInstance;
    }

    @Override
    public Integer weight() {
        String weight = serviceInstance.getPayload().getMetadata().get("weight");
        if (StringUtils.isEmpty(weight)) {
            return 100;
        } else {
            return Integer.valueOf(weight);
        }
    }
}
