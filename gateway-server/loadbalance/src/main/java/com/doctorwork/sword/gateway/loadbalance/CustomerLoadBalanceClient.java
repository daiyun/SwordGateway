package com.doctorwork.sword.gateway.loadbalance;

import com.doctorwork.sword.gateway.common.config.ILoadBalancerConfigRepository;
import com.doctorwork.sword.gateway.common.config.LoadBalancerInfo;
import com.doctorwork.sword.gateway.common.event.AbstractEvent;
import com.doctorwork.sword.gateway.common.event.LoadBalanceConfigDeleteEvent;
import com.doctorwork.sword.gateway.common.event.LoadBalanceConfigLoadEvent;
import com.doctorwork.sword.gateway.common.event.ServiceCacheChangeEvent;
import com.doctorwork.sword.gateway.common.listener.EventListener;
import com.doctorwork.sword.gateway.discovery.IDiscoveryRepository;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.doctorwork.sword.gateway.loadbalance.server.CompositiveServerList;
import com.doctorwork.sword.gateway.loadbalance.server.DataBaseServerList;
import com.doctorwork.sword.gateway.loadbalance.server.ZookeeperServerList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
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
public class CustomerLoadBalanceClient extends AbstractLoadBalanceClient implements ILoadBalanceClientManagerApi, EventListener<AbstractEvent> {

    private final Map<String, BaseLoadBalancer> loadBalancerMap = new ConcurrentHashMap<>();


    private ILoadBalancerConfigRepository loadBalancerConfigRepository;
    private IDiscoveryRepository iDiscoveryRepository;
    private EventBus eventBus;

    private final StampedLock stampedLock = new StampedLock();

    public CustomerLoadBalanceClient(ILoadBalancerConfigRepository loadBalancerConfigRepository, IDiscoveryRepository iDiscoveryRepository, EventBus eventBus) {
        this.loadBalancerConfigRepository = loadBalancerConfigRepository;
        this.iDiscoveryRepository = iDiscoveryRepository;
        this.eventBus = eventBus;
        this.register(this.eventBus);
    }

    @Override
    protected Server getServer(String serviceId) {
        ILoadBalancer loadBalancer = this.getLoadBalancer(serviceId);

        return loadBalancer == null ? null : loadBalancer.chooseServer(serviceId);
    }

    @Override
    protected ILoadBalancer getLoadBalancer(String serviceId) {
        long stamp = stampedLock.tryOptimisticRead();
        BaseLoadBalancer loadBalancer = loadBalancerMap.get(serviceId);
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
            LoadBalancerInfo loadbalanceInfo = loadBalancerConfigRepository.loadbalanceConfig(lbMark);
            if (loadbalanceInfo == null) {
                logger.info(logPrex + "无配置信息，无法初始化");
                return;
            }
            BaseLoadBalancer loadBalancer = loadBalancerMap.get(lbMark);
            if (loadBalancer != null) {
                logger.info(logPrex + "负载已载入，无需初始化");
                return;
            }
            BaseLoadBalancer baseLoadBalancer = loadBalancer(lbMark, loadbalanceInfo);
            loadBalancerMap.putIfAbsent(lbMark, baseLoadBalancer);
            logger.info(logPrex + "Done");
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    private BaseLoadBalancer loadBalancer(String lbMark, LoadBalancerInfo loadbalanceInfo) {
        ServerList serverList;
        boolean dscrEnable = loadbalanceInfo.getDscrEnable() != null && loadbalanceInfo.getDscrEnable().equals(1);
        if (!dscrEnable) {
            serverList = new CompositiveServerList(lbMark, new DataBaseServerList(lbMark, loadBalancerConfigRepository));
        } else {
            serverList = new CompositiveServerList(lbMark, true, new DataBaseServerList(lbMark, loadBalancerConfigRepository),
                    new ZookeeperServerList(lbMark, iDiscoveryRepository));
        }
        DynamicLoadBalancer dynamicLoadBalancer = new DynamicLoadBalancer(serverList);
        dynamicLoadBalancer.init(loadbalanceInfo);
        return dynamicLoadBalancer;
    }

    @Override
    public void loadBalanceLoad(String lbMark) {
        if (StringUtils.isEmpty(lbMark)) {
            return;
        }
        String logPrex = "【重载负载均衡器】[" + lbMark + "]";
        LoadBalancerInfo lbInfo = loadBalancerConfigRepository.loadbalanceConfig(lbMark);

        long stamp = stampedLock.writeLock();
        try {
            BaseLoadBalancer old = loadBalancerMap.get(lbMark);
            if (lbInfo == null && old == null) {
                logger.info(logPrex + "无需载入");
                return;
            }
            if (lbInfo == null) {
                old.shutdown();
                loadBalancerMap.remove(lbMark);
                logger.info(logPrex + "关闭负载器");
                return;
            }
            BaseLoadBalancer baseLoadBalancer = loadBalancer(lbMark, lbInfo);
            loadBalancerMap.put(lbMark, baseLoadBalancer);
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
        LoadBalancerInfo lbInfo = loadBalancerConfigRepository.loadbalanceConfig(lbMark);
        if (lbInfo == null) {
            logger.info(logPrex + "策略未找到");
            return;
        }
        if (loadBalancer instanceof DynamicLoadBalancer) {
            DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
            dynamicLoadBalancer.reloadPing(lbInfo);
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
        LoadBalancerInfo lbInfo = loadBalancerConfigRepository.loadbalanceConfig(lbMark);
        if (lbInfo == null) {
            logger.info(logPrex + "策略未找到");
            return;
        }
        if (loadBalancer instanceof DynamicLoadBalancer) {
            DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
            dynamicLoadBalancer.reloadRule(lbInfo);
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
        LoadBalancerInfo lbInfo = loadBalancerConfigRepository.loadbalanceConfig(lbMark);
        if (lbInfo == null) {
            logger.info(logPrex + "策略未找到");
            return;
        }
        if (loadBalancer instanceof DynamicLoadBalancer) {
            DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
            dynamicLoadBalancer.reloadAutoRefresh(lbInfo);
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
        LoadBalancerInfo lbInfo = loadBalancerConfigRepository.loadbalanceConfig(lbMark);
        if (lbInfo == null) {
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
                boolean dscrEnable = lbInfo.getDscrEnable() != null && lbInfo.getDscrEnable().equals(1);
                if (oldServerList == null) {
                    logger.info(logPrex + "负载器服务列表异常");
                    return;
                }
                CompositiveServerList serverList = (CompositiveServerList) oldServerList;
                if (!dscrEnable) {
                    serverList.discoveryReload(false, new DataBaseServerList(lbMark, loadBalancerConfigRepository), null);
                } else {
                    serverList.discoveryReload(true, new DataBaseServerList(lbMark, loadBalancerConfigRepository),
                            new ZookeeperServerList(lbMark, iDiscoveryRepository));
                }
                logger.info(logPrex + "Done");
            }
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    @Override
    @Subscribe
    public void handleEvent(AbstractEvent event) {
        if (event instanceof ServiceCacheChangeEvent) {
            ServiceCacheChangeEvent cacheChangeEvent = (ServiceCacheChangeEvent) event;
            String serviceId = cacheChangeEvent.getServiceId();
            logger.info("[ServiceCacheChangeEvent]handle event for {}", serviceId);
            BaseLoadBalancer loadBalancer = loadBalancerMap.get(serviceId);
            if (loadBalancer == null) {
                logger.info("服务提供者[{}]并未开启负载均衡", serviceId);
                return;
            }
            if (loadBalancer instanceof DynamicLoadBalancer) {
                DynamicLoadBalancer dynamicLoadBalancer = (DynamicLoadBalancer) loadBalancer;
                logger.info("更新负载均衡器[{}]的服务列表", serviceId);
                dynamicLoadBalancer.updateListOfServers(true);
            }
        } else if (event instanceof LoadBalanceConfigDeleteEvent) {
            LoadBalanceConfigDeleteEvent configLoadEvent = (LoadBalanceConfigDeleteEvent) event;
            String lbMark = configLoadEvent.getLbMark();
            logger.info("[LoadBalanceConfigDeleteEvent]handle event for {}", lbMark);
            this.loadBalanceDelete(lbMark);
        } else if (event instanceof LoadBalanceConfigLoadEvent) {
            LoadBalanceConfigLoadEvent configLoadEvent = (LoadBalanceConfigLoadEvent) event;
            String lbMark = configLoadEvent.getLbMark();
            logger.info("[LoadBalanceConfigLoadEvent]handle event for {}", lbMark);
            this.loadBalanceLoad(lbMark);
        }
    }
}
