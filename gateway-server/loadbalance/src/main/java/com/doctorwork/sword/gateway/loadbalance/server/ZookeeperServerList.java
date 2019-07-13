package com.doctorwork.sword.gateway.loadbalance.server;

import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.discovery.IDiscoveryRepository;
import com.doctorwork.sword.gateway.discovery.common.AppStatusEnum;
import com.doctorwork.sword.gateway.discovery.common.Constants;
import com.doctorwork.sword.gateway.discovery.common.ZookeeperInstance;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:26 2019/7/2
 * @Modified By:
 */
public class ZookeeperServerList extends CustomerServerList<ZookeeperServer> {

    private IDiscoveryRepository iDiscoveryRepository;

    public ZookeeperServerList(String serviceId, IDiscoveryRepository iDiscoveryRepository) {
        super(serviceId);
        this.iDiscoveryRepository = iDiscoveryRepository;
    }

    @Override
    public List<ZookeeperServer> getInitialListOfServers() {
        List<ZookeeperServer> dataBaseServers = getServers();
        logger.info("获取初始化Zookeeper负载器{}服务列表 \n--服务数量{}\n--服务列表:{}", getServiceId(), dataBaseServers.size(), JacksonUtil.toJSon(dataBaseServers));
        return dataBaseServers;
    }

    @Override
    public List<ZookeeperServer> getUpdatedListOfServers() {
        List<ZookeeperServer> dataBaseServers = getServers();
        logger.info("获取更新Zookeeper负载器{}服务列表 \n--服务数量{}\n--服务列表:{}", getServiceId(), dataBaseServers.size(), JacksonUtil.toJSon(dataBaseServers));
        return dataBaseServers;
    }

    protected List<ZookeeperServer> getServers() {
        try {
            Collection<ServiceInstance<ZookeeperInstance>> instances = iDiscoveryRepository.queryServices(getServiceId());
            if (CollectionUtils.isEmpty(instances))
                return Collections.emptyList();
            List<ZookeeperServer> servers = new ArrayList<>();
            for (ServiceInstance<ZookeeperInstance> instance : instances) {
                String instanceStatus = null;
                if (instance.getPayload() != null && instance.getPayload().getMetadata() != null) {
                    instanceStatus = instance.getPayload().getMetadata().get(Constants.APP_STATUS_ZK_KEY);
                }
                if (!StringUtils.hasText(instanceStatus) // backwards compatibility
                        || instanceStatus.equalsIgnoreCase(AppStatusEnum.ON.name())) {
                    servers.add(new ZookeeperServer(instance));
                }
            }
            return servers;
        } catch (Exception e) {
            rethrowRuntimeException(e);
        }
        return Collections.emptyList();
    }

    @Override
    public void clear() {
        this.iDiscoveryRepository = null;
        super.setServiceId(null);
    }
}
