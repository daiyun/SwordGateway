package com.doctorwork.sword.gateway.loadbalance.server;

import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.common.config.ILoadBalancerConfigRepository;
import com.doctorwork.sword.gateway.common.config.LoadBalancerServer;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:04 2019/6/18
 * @Modified By:
 */
public class DataBaseServerList extends CustomerServerList<DataBaseServer> {

    private ILoadBalancerConfigRepository loadBalancerConfigRepository;

    public DataBaseServerList(String serviceId, ILoadBalancerConfigRepository loadBalancerConfigRepository) {
        super(serviceId);
        this.loadBalancerConfigRepository = loadBalancerConfigRepository;
    }

    @Override
    public List<DataBaseServer> getInitialListOfServers() {
        List<DataBaseServer> dataBaseServers = getServers(this.loadBalancerConfigRepository);
        logger.info("获取初始化DB负载器{}服务列表 \n--服务数量{}\n--服务列表:{}", getServiceId(), dataBaseServers.size(), JacksonUtil.toJSon(dataBaseServers));
        return dataBaseServers;
    }

    @Override
    public List<DataBaseServer> getUpdatedListOfServers() {
        List<DataBaseServer> dataBaseServers = getServers(this.loadBalancerConfigRepository);
        logger.info("获取更新DB负载器{}服务列表 \n--服务数量{}\n--服务列表:{}", getServiceId(), dataBaseServers.size(), JacksonUtil.toJSon(dataBaseServers));
        return dataBaseServers;
    }

    @SuppressWarnings("unchecked")
    protected List<DataBaseServer> getServers(ILoadBalancerConfigRepository loadBalancerConfigRepository) {
        try {
            Collection<LoadBalancerServer> servers = loadBalancerConfigRepository.loadbalanceServer(getServiceId());
            if (CollectionUtils.isEmpty(servers)) {
                return Collections.emptyList();
            }
            List<DataBaseServer> list = new ArrayList<>();
            for (LoadBalancerServer loadbalanceServer : servers) {
                DataBaseServer dataBaseServer = new DataBaseServer(loadbalanceServer);
                list.add(dataBaseServer);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
