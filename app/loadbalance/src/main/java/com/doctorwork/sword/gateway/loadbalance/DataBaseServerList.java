package com.doctorwork.sword.gateway.loadbalance;

import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.dal.model.LoadbalanceServer;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:04 2019/6/18
 * @Modified By:
 */
public class DataBaseServerList extends AbstractServerList<DataBaseServer> {

    private static final Logger logger = LoggerFactory.getLogger(DataBaseServerList.class);

    private String serviceId;
    private GatewayLoadBalanceService gatewayLoadBalanceService;

    public DataBaseServerList(GatewayLoadBalanceService gatewayLoadBalanceService) {
        this.gatewayLoadBalanceService = gatewayLoadBalanceService;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        this.serviceId = clientConfig.getClientName();
    }

    public void initFromDependencies(IClientConfig clientConfig) {
        this.serviceId = clientConfig.getClientName();
    }

    @Override
    public List<DataBaseServer> getInitialListOfServers() {
        List<DataBaseServer> dataBaseServers = getServers(this.gatewayLoadBalanceService);
        logger.info("初始化负载器{}服务列表 \n--服务数量{}\n--服务列表:{}", serviceId, dataBaseServers.size(), JacksonUtil.toJSon(dataBaseServers));
        return dataBaseServers;
    }

    @Override
    public List<DataBaseServer> getUpdatedListOfServers() {
        List<DataBaseServer> dataBaseServers = getServers(this.gatewayLoadBalanceService);
        logger.info("更新负载器{}服务列表 \n--服务数量{}\n--服务列表:{}", serviceId, dataBaseServers.size(), JacksonUtil.toJSon(dataBaseServers));
        return dataBaseServers;
    }

    @SuppressWarnings("unchecked")
    protected List<DataBaseServer> getServers(GatewayLoadBalanceService gatewayLoadBalanceService) {
        try {
            List<LoadbalanceServer> servers = gatewayLoadBalanceService.loadBalanceServers(this.serviceId);
            if (CollectionUtils.isEmpty(servers)) {
                return Collections.emptyList();
            }
            return servers.stream().map(loadbalanceServer -> new DataBaseServer(loadbalanceServer)).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
