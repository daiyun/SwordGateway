package com.doctorwork.sword.gateway.loadbalance;

import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.discovery.IDiscoveryRepository;
import com.doctorwork.sword.gateway.discovery.ServiceWrapper;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.doctorwork.sword.gateway.loadbalance.server.CompositiveServerList;
import com.doctorwork.sword.gateway.loadbalance.server.DataBaseServerList;
import com.doctorwork.sword.gateway.loadbalance.server.ZookeeperServerList;
import com.doctorwork.sword.gateway.service.GatewayLoadBalanceService;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.StampedLock;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:27 2019/7/2
 * @Modified By:
 */
public class CustomerLoadBalanceClient extends AbstractLoadBalanceClient implements ILoadBalanceClientManagerApi {

    private final Map<String, BaseLoadBalancer> loadBalancerMap = new ConcurrentHashMap<>();


    private GatewayLoadBalanceService gatewayLoadBalanceService;
    private IDiscoveryRepository iDiscoveryRepository;

    private final StampedLock stampedLock = new StampedLock();

    public CustomerLoadBalanceClient(GatewayLoadBalanceService gatewayLoadBalanceService, IDiscoveryRepository iDiscoveryRepository) {
        this.gatewayLoadBalanceService = gatewayLoadBalanceService;
        this.iDiscoveryRepository = iDiscoveryRepository;
    }

    @Override
    protected Server getServer(String serviceId) {
        ILoadBalancer loadBalancer = this.getLoadBalancer(serviceId);

        return loadBalancer == null ? null : loadBalancer.chooseServer(serviceId);
    }

    @Override
    protected ILoadBalancer getLoadBalancer(String serviceId) {
        long stamp = stampedLock.tryOptimisticRead();
        ILoadBalancer loadBalancer = loadBalancerMap.get(serviceId);
        if (loadBalancer == null) {
            this.loadBalanceInit(serviceId);
        }
        if (!stampedLock.validate(stamp)) {
            stamp = stampedLock.readLock();
            try {
                loadBalancer = loadBalancerMap.get(serviceId);
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        return loadBalancer;
    }

    @Override
    public void loadBalanceInit(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        String logPrex = "【初始化负载均衡器】[" + lbMark + "]";
        long stamp = stampedLock.writeLock();
        try {
            LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(lbMark);
            if (loadbalanceInfo == null) {
                logger.info(logPrex + "无需初始化");
                return;
            }
            ILoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
            if (loadBalancer != null) {
                logger.info(logPrex + "负载已载入，无需初始化");
                return;
            }
            ServerList serverList;
            Boolean dscrEnable = loadbalanceInfo.getDscrEnable() != null && loadbalanceInfo.getDscrEnable().equals(1);
            if (!dscrEnable) {
                serverList = new CompositiveServerList(lbMark, new DataBaseServerList(lbMark, gatewayLoadBalanceService));
            } else {
                ServiceWrapper wrapper = iDiscoveryRepository.serviceWrapper(lbMark);
                serverList = new CompositiveServerList(lbMark, true, new DataBaseServerList(lbMark, gatewayLoadBalanceService),
                        new ZookeeperServerList(lbMark, wrapper));
            }
            DynamicLoadBalancer dynamicLoadBalancer = new DynamicLoadBalancer(serverList);
            dynamicLoadBalancer.init(loadbalanceInfo);
            loadBalancerMap.putIfAbsent(lbMark, dynamicLoadBalancer);
            logger.info(logPrex + "Done");
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    @Override
    public void loadBalanceLoad(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        String logPrex = "【重载负载均衡器】[" + lbMark + "]";
        LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(lbMark);

        long stamp = stampedLock.writeLock();
        try {
            ServerList serverList;
            BaseLoadBalancer old = loadBalancerMap.get(lbMark);
            if (loadbalanceInfo == null && old == null) {
                logger.info(logPrex + "无需载入");
                return;
            }
            if (loadbalanceInfo == null) {
                old.shutdown();
                loadBalancerMap.remove(lbMark);
                logger.info(logPrex + "关闭负载器");
                return;
            }
            boolean dscrEnable = loadbalanceInfo.getDscrEnable() != null && loadbalanceInfo.getDscrEnable().equals(1);
            if (!dscrEnable) {
                serverList = new CompositiveServerList(lbMark, new DataBaseServerList(lbMark, gatewayLoadBalanceService));
            } else {
                ServiceWrapper wrapper = iDiscoveryRepository.serviceWrapper(lbMark);
                serverList = new CompositiveServerList(lbMark, true, new DataBaseServerList(lbMark, gatewayLoadBalanceService),
                        new ZookeeperServerList(lbMark, wrapper));
            }
            DynamicLoadBalancer dynamicLoadBalancer = new DynamicLoadBalancer(serverList);
            dynamicLoadBalancer.init(loadbalanceInfo);
            loadBalancerMap.put(lbMark, dynamicLoadBalancer);
            old.shutdown();
            logger.info(logPrex + "Done");
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    @Override
    public void loadBalanceDelete(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        String logPrex = "【关闭负载均衡器】[" + lbMark + "]";
        BaseLoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
        if (loadBalancer == null) {
            logger.info(logPrex + "无需关闭");
            return;
        }
        loadBalancer.shutdown();
        loadBalancerMap.remove(lbMark);
        logger.info(logPrex + "Done");
    }

    @Override
    public void loadBalancePingLoad(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        String logPrex = "【负载均衡器PING策略重载】[" + lbMark + "]";
        BaseLoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
        if (loadBalancer == null) {
            logger.info(logPrex + "负载器未加载无法重载");
            return;
        }
        LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(lbMark);
        if (loadbalanceInfo == null) {
            logger.info(logPrex + "策略未找到");
            return;
        }
        if (loadBalancer instanceof DynamicLoadBalancer) {
            DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
            dynamicLoadBalancer.reloadPing(loadbalanceInfo);
        }
        logger.info(logPrex + "Done");
    }

    @Override
    public void loadBalanceRuleLoad(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        String logPrex = "【负载均衡器RULE策略重载】[" + lbMark + "]";
        BaseLoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
        if (loadBalancer == null) {
            logger.info(logPrex + "负载器未加载无法重载");
            return;
        }
        LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(lbMark);
        if (loadbalanceInfo == null) {
            logger.info(logPrex + "策略未找到");
            return;
        }
        if (loadBalancer instanceof DynamicLoadBalancer) {
            DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
            dynamicLoadBalancer.reloadRule(loadbalanceInfo);
        }
        logger.info(logPrex + "Done");
    }

    @Override
    public void loadBalanceAutoRefreshLoad(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        String logPrex = "【负载均衡器自动刷新策略重载】[" + lbMark + "]";
        BaseLoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
        if (loadBalancer == null) {
            logger.info(logPrex + "负载器未加载无法重载");
            return;
        }
        LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(lbMark);
        if (loadbalanceInfo == null) {
            logger.info(logPrex + "策略未找到");
            return;
        }
        if (loadBalancer instanceof DynamicLoadBalancer) {
            DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
            dynamicLoadBalancer.reloadAutoRefresh(loadbalanceInfo);
        }
        logger.info(logPrex + "Done");
    }

    @Override
    public void loadBalanceAutoRefreshShutdown(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        String logPrex = "【负载均衡器自动刷新策略关闭】[" + lbMark + "]";
        BaseLoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
        if (loadBalancer == null) {
            logger.info(logPrex + "负载器未加载无法重载");
            return;
        }
        if (loadBalancer instanceof DynamicLoadBalancer) {
            DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
            dynamicLoadBalancer.stopServerListRefreshing();
        }
        logger.info(logPrex + "Done");
    }

    @Override
    public void loadBalanceDiscoveryLoad(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        String logPrex = "【负载均衡器服务发现模块重载】[" + lbMark + "]";
        LoadbalanceInfo loadbalanceInfo = gatewayLoadBalanceService.loadBalance(lbMark);
        if (loadbalanceInfo == null) {
            logger.info(logPrex + "负载器信息未找到");
            return;
        }
        BaseLoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
        if (loadBalancer == null) {
            logger.info(logPrex + "负载器未加载无法重载");
            return;
        }
        long stamp = stampedLock.writeLock();
        try {
            if (loadBalancer instanceof DynamicLoadBalancer) {
                DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
                ServerList oldServerList = dynamicLoadBalancer.getServerListImpl();
                boolean dscrEnable = loadbalanceInfo.getDscrEnable() != null && loadbalanceInfo.getDscrEnable().equals(1);
                if (oldServerList == null) {
                    logger.info(logPrex + "负载器服务列表异常");
                    return;
                }
                CompositiveServerList serverList = (CompositiveServerList) oldServerList;
                if (!dscrEnable) {
                    serverList.discoveryReload(false, new DataBaseServerList(lbMark, gatewayLoadBalanceService), null);
                } else {
                    ServiceWrapper wrapper = iDiscoveryRepository.serviceWrapper(lbMark);
                    serverList.discoveryReload(true, new DataBaseServerList(lbMark, gatewayLoadBalanceService), new ZookeeperServerList(lbMark, wrapper));
                }
                logger.info(logPrex + "Done");
            }
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }
}
