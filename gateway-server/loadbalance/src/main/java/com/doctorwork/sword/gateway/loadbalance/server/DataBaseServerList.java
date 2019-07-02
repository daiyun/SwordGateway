package com.doctorwork.sword.gateway.loadbalance.server;

import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.dal.model.LoadbalanceServer;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:04 2019/6/18
 * @Modified By:
 */
public class DataBaseServerList extends CustomerServerList<DataBaseServer> {

    private GatewayLoadBalanceService gatewayLoadBalanceService;

    public DataBaseServerList(GatewayLoadBalanceService gatewayLoadBalanceService, String serviceId) {
        super(serviceId);
        this.gatewayLoadBalanceService = gatewayLoadBalanceService;
    }

    @Override
    public List<DataBaseServer> getInitialListOfServers() {
        List<DataBaseServer> dataBaseServers = getServers(this.gatewayLoadBalanceService);
        logger.info("获取初始化DB负载器{}服务列表 \n--服务数量{}\n--服务列表:{}", getServiceId(), dataBaseServers.size(), JacksonUtil.toJSon(dataBaseServers));
        return dataBaseServers;
    }

    @Override
    public List<DataBaseServer> getUpdatedListOfServers() {
        List<DataBaseServer> dataBaseServers = getServers(this.gatewayLoadBalanceService);
        logger.info("获取更新DB负载器{}服务列表 \n--服务数量{}\n--服务列表:{}", getServiceId(), dataBaseServers.size(), JacksonUtil.toJSon(dataBaseServers));
        return dataBaseServers;
    }

    @SuppressWarnings("unchecked")
    protected List<DataBaseServer> getServers(GatewayLoadBalanceService gatewayLoadBalanceService) {
        try {
            List<LoadbalanceServer> servers = gatewayLoadBalanceService.loadBalanceServers(getServiceId());
            if (CollectionUtils.isEmpty(servers)) {
                return Collections.emptyList();
            }
            List<DataBaseServer> list = new ArrayList<>();
            for (LoadbalanceServer loadbalanceServer : servers) {
                DataBaseServer dataBaseServer = new DataBaseServer(loadbalanceServer);
                list.add(dataBaseServer);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
