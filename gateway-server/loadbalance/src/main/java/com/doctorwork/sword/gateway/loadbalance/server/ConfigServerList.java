package com.doctorwork.sword.gateway.loadbalance.server;

import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.common.config.ILoadBalancerConfigRepository;
import com.doctorwork.sword.gateway.common.config.LoadBalancerServer;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:04 2019/6/18
 * @Modified By:
 */
public class ConfigServerList extends CustomerServerList<ConfigServer> {

    private ILoadBalancerConfigRepository loadBalancerConfigRepository;

    public ConfigServerList(String serviceId, ILoadBalancerConfigRepository loadBalancerConfigRepository) {
        super(serviceId);
        this.loadBalancerConfigRepository = loadBalancerConfigRepository;
    }

    @Override
    public List<ConfigServer> getInitialListOfServers() {
        List<ConfigServer> configServers = getServers(this.loadBalancerConfigRepository, true);
        logger.info("获取初始化DB负载器{}服务列表 \n--服务数量{}\n--服务列表:{}", getServiceId(), configServers.size(), JacksonUtil.toJSon(configServers));
        return configServers;
    }

    @Override
    public List<ConfigServer> getUpdatedListOfServers() {
        List<ConfigServer> configServers = getServers(this.loadBalancerConfigRepository, true);
        logger.info("获取更新DB负载器{}服务列表 \n--服务数量{}\n--服务列表:{}", getServiceId(), configServers.size(), JacksonUtil.toJSon(configServers));
        return configServers;
    }

    @SuppressWarnings("unchecked")
    List<ConfigServer> getServers(ILoadBalancerConfigRepository loadBalancerConfigRepository, boolean valid) {
        try {
            Collection<LoadBalancerServer> servers = loadBalancerConfigRepository.loadbalanceServer(getServiceId());
            if (CollectionUtils.isEmpty(servers)) {
                return Collections.emptyList();
            }
            List<ConfigServer> list = new ArrayList<>();
            if (valid) {
                for (LoadBalancerServer loadbalanceServer : servers) {
                    if (!loadbalanceServer.serverValid())
                        continue;
                    ConfigServer configServer = new ConfigServer(loadbalanceServer);
                    list.add(configServer);
                }
            } else {
                for (LoadBalancerServer loadbalanceServer : servers) {
                    ConfigServer configServer = new ConfigServer(loadbalanceServer);
                    list.add(configServer);
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ConfigServer> getServer(boolean valid) {
        return getServers(this.loadBalancerConfigRepository, valid);
    }

    @Override
    public void clear() {
        this.loadBalancerConfigRepository = null;
        super.setServiceId(null);
    }
}
