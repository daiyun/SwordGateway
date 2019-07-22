package com.doctorwork.sword.gateway.config;

import com.doctorwork.com.sword.gateway.registry.IRegistryConnectionRepository;
import com.doctorwork.com.sword.gateway.registry.RegistryConnectionRepositoryManager;
import com.doctorwork.com.sword.gateway.registry.wrapper.ConnectionWrapper;
import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.common.config.*;
import com.doctorwork.sword.gateway.common.event.*;
import com.doctorwork.sword.gateway.common.listener.EventListener;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.StampedLock;

/**
 * @author chenzhiqiang
 * @date 2019/7/8
 */
public class RegistryConfigRepository extends AbstractConfiguration implements EventPost, EventListener<AbstractEvent> {
    private IRegistryConnectionRepository registryConnectionRepository;
    private EventBus eventBus;
    private static final String REGISTRY_PATH = "/doctorwork/gateway/configuration";
    private PathChildrenCache discoveryCache = null;
    private PathChildrenCache registryCache = null;
    private PathChildrenCache loadbalanceCache = null;
    private PathChildrenCache routeCache = null;
    private final AtomicBoolean init = new AtomicBoolean(false);
    private StampedLock stampedLock = new StampedLock();

    private static final String REGISTRY_NODE = REGISTRY_PATH + "/registry/";
    private static final String LOADBALANCE_NODE = REGISTRY_PATH + "/loadbalance/";
    private static final String DISCOVERY_NODE = REGISTRY_PATH + "/discovery/";
    private static final String LOADBALANCE_SERVER_NODE = REGISTRY_PATH + "/loadbalance-server/";
    private static final String ROUTE_NODE = REGISTRY_PATH + "/route/";
    private static final String ROUTE_PREDICATION_NODE = REGISTRY_PATH + "/route-predication/";
    private static final String ROUTE_FILTER_NODE = REGISTRY_PATH + "/route-filter/";


    public RegistryConfigRepository(IRegistryConnectionRepository registryConnectionRepository, EventBus eventBus, GatewayConfig gatewayConfig) {
        super(gatewayConfig);
        this.registryConnectionRepository = registryConnectionRepository;
        this.eventBus = eventBus;
        register(this.eventBus);
    }

    public void init() throws Exception {
        if (init.get())
            return;
        if (!init.compareAndSet(false, true))
            return;
        ConnectionWrapper connectionWrapper = registryConnectionRepository.connection(RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
        if (connectionWrapper == null) {
            //try to load default registry
            registryConnectionRepository.connectionLoad(RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
            connectionWrapper = registryConnectionRepository.connection(RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
            Assert.notNull(connectionWrapper, "can not load default registry[close use registry in com.doctorwork.sword.gateway.config.GatewayConfig if could]");
        }
        this.cacheReload(connectionWrapper);
    }

    private void cacheReload(ConnectionWrapper connectionWrapper) throws Exception {
        if (connectionWrapper == null) {
            logger.error("no connection, discovery,registry and loadbalance cache can not reload");
            return;
        }
        //对服务发现路径进行节点监听达到感知服务发现节点
        if (discoveryCache != null) {
            CloseableUtils.closeQuietly(discoveryCache);
        }
        discoveryCache = new PathChildrenCache(connectionWrapper.getConnection(CuratorFramework.class), DISCOVERY_NODE.substring(0, DISCOVERY_NODE.length() - 1), true);
        discoveryCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                String node = ZKPaths.getNodeFromPath(event.getData().getPath());
                ChildData data = event.getData();
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        logger.info("服务发现节点{}增加 ", node);
                        eventPost(new DiscoverConfigLoadEvent(node, data.getStat().getVersion()));
                        break;
                    }

                    case CHILD_UPDATED: {
                        logger.info("服务发现节点{}更新", node);
                        eventPost(new DiscoverConfigLoadEvent(node, data.getStat().getVersion()));
                        break;
                    }

                    case CHILD_REMOVED: {
                        logger.info("服务发现节点{}移除", node);
                        eventPost(new DiscoverConfigDeleteEvent(node));
                        break;
                    }
                }
            }
        });
        discoveryCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        if (registryCache != null) {
            CloseableUtils.closeQuietly(registryCache);
        }
        registryCache = new PathChildrenCache(connectionWrapper.getConnection(CuratorFramework.class), REGISTRY_NODE.substring(0, REGISTRY_NODE.length() - 1), true);
        registryCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                String node = ZKPaths.getNodeFromPath(event.getData().getPath());
                ChildData data = event.getData();
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        logger.info("注册中心节点{}增加 ", node);
                        eventPost(new RegistryConfigLoadEvent(node, data.getStat().getVersion()));
                        break;
                    }

                    case CHILD_UPDATED: {
                        logger.info("注册中心节点{}更新", node);
                        eventPost(new RegistryConfigLoadEvent(node, data.getStat().getVersion()));
                        break;
                    }

                    case CHILD_REMOVED: {
                        logger.info("注册中心节点{}移除", node);
                        eventPost(new RegistryConfigDeleteEvent(node));
                        break;
                    }
                }
            }
        });
        registryCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        if (loadbalanceCache != null) {
            CloseableUtils.closeQuietly(loadbalanceCache);
        }
        loadbalanceCache = new PathChildrenCache(connectionWrapper.getConnection(CuratorFramework.class), LOADBALANCE_NODE.substring(0, LOADBALANCE_NODE.length() - 1), true);
        loadbalanceCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                String node = ZKPaths.getNodeFromPath(event.getData().getPath());
                ChildData data = event.getData();
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        logger.info("负载均衡器节点{}增加 ", node);
                        eventPost(new LoadBalanceConfigLoadEvent(node, data.getStat().getVersion()));
                        break;
                    }

                    case CHILD_UPDATED: {
                        logger.info("负载均衡器节点{}更新", node);
                        eventPost(new LoadBalanceConfigLoadEvent(node, data.getStat().getVersion()));
                        break;
                    }

                    case CHILD_REMOVED: {
                        logger.info("负载均衡器节点{}移除", node);
                        eventPost(new LoadBalanceConfigDeleteEvent(node));
                        break;
                    }
                }
            }
        });
        loadbalanceCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        routeCache = new PathChildrenCache(connectionWrapper.getConnection(CuratorFramework.class), ROUTE_NODE.substring(0, ROUTE_NODE.length() - 1), true);
        routeCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                String node = ZKPaths.getNodeFromPath(event.getData().getPath());
                ChildData data = event.getData();
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        logger.info("路由节点节点{}增加 ", node);
                        eventPost(new RouteConfigLoadEvent(node));
                        break;
                    }

                    case CHILD_UPDATED: {
                        logger.info("路由节点节点{}更新", node);
                        eventPost(new RouteConfigLoadEvent(node));
                        break;
                    }

                    case CHILD_REMOVED: {
                        logger.info("路由节点节点{}移除", node);
                        eventPost(new RouteConfigDeleteEvent(node));
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void eventPost(AbstractEvent event) {
        if (this.eventBus != null) {
            logger.info("post event {}", event.getClass());
            eventBus.post(event);
        }
    }

    @Override
    @Subscribe
    public void handleEvent(AbstractEvent event) {
        if (event instanceof RegistryLoadEvent) {
            RegistryLoadEvent registryLoadEvent = (RegistryLoadEvent) event;
            String registryId = registryLoadEvent.getRegistryId();
            logger.info("[RegistryLoadEvent] handle event for {}", registryId);
            if (RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER.equals(registryId) && init.get()) {
                long stamp = stampedLock.writeLock();
                try {
                    //reload default zookeeper,reload node
                    ConnectionWrapper connectionWrapper = registryConnectionRepository.connection(RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
                    this.cacheReload(connectionWrapper);
                } catch (Exception e) {
                    logger.error("[RegistryLoadEvent] error happened for {}", registryId, e);
                } finally {
                    stampedLock.unlockWrite(stamp);
                }

            }
        }
    }

    @Override
    public ConnectionInfo connectionConfig(String registryId) {
        long stamp = stampedLock.tryOptimisticRead();
        byte[] datas;
        ChildData data = registryCache.getCurrentData(REGISTRY_NODE.concat(registryId));
        if (!stampedLock.validate(stamp)) {
            stampedLock.readLock();
            try {
                data = registryCache.getCurrentData(REGISTRY_NODE.concat(registryId));
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        if (data == null)
            return null;
        datas = data.getData();
        if (datas != null) {
            return JacksonUtil.toObject(datas, ConnectionInfo.class);
        }
        return null;
    }

    @Override
    public DiscoveryInfo discoveryConfig(String dscrId) {
        long stamp = stampedLock.tryOptimisticRead();
        byte[] datas;
        ChildData data = discoveryCache.getCurrentData(DISCOVERY_NODE.concat(dscrId));
        if (!stampedLock.validate(stamp)) {
            stampedLock.readLock();
            try {
                data = discoveryCache.getCurrentData(DISCOVERY_NODE.concat(dscrId));
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        if (data == null)
            return null;
        datas = data.getData();
        if (datas != null) {
            return JacksonUtil.toObject(datas, DiscoveryInfo.class);
        }
        return null;
    }

    @Override
    public DiscoveryInfo discoveryConfigFromLoadBalance(String lbMark) {
        LoadBalancerInfo loadbalanceInfo = this.loadbalanceConfig(lbMark);
        if (loadbalanceInfo == null || loadbalanceInfo.getDscrEnable().equals(0) || StringUtils.isEmpty(loadbalanceInfo.getDiscoveryId()))
            return null;
        return this.discoveryConfig(loadbalanceInfo.getDiscoveryId());
    }

    @Override
    public Collection<DiscoveryInfo> all() {
        List<ChildData> childDatas = discoveryCache.getCurrentData();
        if (CollectionUtils.isEmpty(childDatas))
            return Collections.emptyList();
        List<DiscoveryInfo> discoveryInfos = new ArrayList<>(childDatas.size());
        for (ChildData childData : childDatas) {
            byte[] bytes = childData.getData();
            DiscoveryInfo discoveryInfo = JacksonUtil.toObject(bytes, DiscoveryInfo.class);
            discoveryInfos.add(discoveryInfo);
        }
        return discoveryInfos;
    }

    @Override
    public LoadBalancerInfo loadbalanceConfig(String lbMark) {
        long stamp = stampedLock.tryOptimisticRead();
        byte[] datas;
        ChildData data = loadbalanceCache.getCurrentData(LOADBALANCE_NODE.concat(lbMark));
        if (!stampedLock.validate(stamp)) {
            stampedLock.readLock();
            try {
                data = loadbalanceCache.getCurrentData(LOADBALANCE_NODE.concat(lbMark));
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        if (data == null)
            return null;
        datas = data.getData();
        if (datas != null) {
            return JacksonUtil.toObject(datas, LoadBalancerInfo.class);
        }
        return null;
    }

    @Override
    public Collection<LoadBalancerServer> loadbalanceServer(String lbMark) {
        ConnectionWrapper connectionWrapper = registryConnectionRepository.connection(RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
        if (connectionWrapper == null) {
            logger.warn("no registry connection");
            return Collections.emptyList();
        }
        String nodePath = LOADBALANCE_SERVER_NODE.concat(lbMark);
        CuratorFramework curatorFramework = connectionWrapper.getConnection(CuratorFramework.class);
        try {
            Stat stat = curatorFramework.checkExists().forPath(nodePath);
            if (stat == null) {
                logger.error("could not get node {} stat", nodePath);
                return Collections.emptyList();
            }
            List<String> nodes = curatorFramework.getChildren().forPath(nodePath);
            if (CollectionUtils.isEmpty(nodes)) {
                return Collections.emptyList();
            }
            List<LoadBalancerServer> servers = new ArrayList<>();
            for (String child : nodes) {
                String childPath = nodePath.concat("/").concat(child);
                byte[] childBytes = curatorFramework.getData().forPath(childPath);
                LoadBalancerServer server = JacksonUtil.toObject(childBytes, LoadBalancerServer.class);
                servers.add(server);
            }
            return servers;
        } catch (Exception e) {
            logger.error("{} config get error", nodePath, e);
        }
        return Collections.emptyList();
    }

    @Override
    public RouteInfo routeInfo(String routeMark) {
        long stamp = stampedLock.tryOptimisticRead();
        byte[] datas;
        ChildData data = routeCache.getCurrentData(ROUTE_NODE.concat(routeMark));
        if (!stampedLock.validate(stamp)) {
            stampedLock.readLock();
            try {
                data = routeCache.getCurrentData(ROUTE_NODE.concat(routeMark));
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        if (data == null)
            return null;
        datas = data.getData();
        if (datas != null) {
            return JacksonUtil.toObject(datas, RouteInfo.class);
        }
        return null;
    }

    @Override
    public Collection<RouteInfo> routeInfos() {
        long stamp = stampedLock.tryOptimisticRead();
        List<ChildData> dataList = routeCache.getCurrentData();
        if (!stampedLock.validate(stamp)) {
            stampedLock.readLock();
            try {
                dataList = routeCache.getCurrentData();
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        if (dataList == null || dataList.size() == 0)
            return null;
        List<RouteInfo> routeInfos = new ArrayList<>(dataList.size());
        for (ChildData data : dataList) {
            byte[] datas = data.getData();
            if (datas != null) {
                routeInfos.add(JacksonUtil.toObject(datas, RouteInfo.class));
            }
        }
        return routeInfos;
    }

    @Override
    public AbstractConfiguration getConfiguration() {
        return this;
    }
}
