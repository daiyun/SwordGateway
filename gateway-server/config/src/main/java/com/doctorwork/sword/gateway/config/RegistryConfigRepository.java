package com.doctorwork.sword.gateway.config;

import com.doctorwork.com.sword.gateway.registry.IRegistryConnectionRepository;
import com.doctorwork.com.sword.gateway.registry.RegistryConnectionRepositoryManager;
import com.doctorwork.com.sword.gateway.registry.wrapper.ConnectionWrapper;
import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.common.event.*;
import com.doctorwork.sword.gateway.common.listener.EventListener;
import com.doctorwork.sword.gateway.dal.model.DiscoverConfig;
import com.doctorwork.sword.gateway.dal.model.DiscoverRegistryConfig;
import com.doctorwork.sword.gateway.dal.model.LoadbalanceInfo;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.google.common.eventbus.EventBus;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author chenzhiqiang
 * @date 2019/7/8
 */
public class RegistryConfigRepository extends AbstractConfiguration implements EventPost, EventListener {
    private IRegistryConnectionRepository registryConnectionRepository;
    private EventBus eventBus;
    private static final String REGISTRY_PATH = "/doctorwork/gateway/configuration";
    private final Map<String, NodeCache> discoveryCacheMap = new ConcurrentHashMap<>();
    private final Map<String, NodeCache> registryCacheMap = new ConcurrentHashMap<>();
    private Map<String, NodeCache> loadbalanceCacheMap = new ConcurrentHashMap<>();
    private final AtomicBoolean init = new AtomicBoolean(false);


    public RegistryConfigRepository(IRegistryConnectionRepository registryConnectionRepository, EventBus eventBus, GatewayConfig gatewayConfig) {
        super(gatewayConfig);
        this.registryConnectionRepository = registryConnectionRepository;
        this.eventBus = eventBus;
        register(this.eventBus);
    }

    public void init() throws IOException {
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
    }

    @Override
    public void eventPost(AbstractEvent event) {
        if (this.eventBus != null)
            eventBus.post(event);
    }

    @Override
    public void handleEvent(AbstractEvent event) {

    }

    @Override
    public DiscoverRegistryConfig connectionConfig(String registryId) {
        ConnectionWrapper connectionWrapper = registryConnectionRepository.connection(RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
        if (connectionWrapper == null) {
            logger.warn("no registry connection");
            return null;
        }
        String nodePath = REGISTRY_PATH.concat("/registry/").concat(registryId);
        NodeCache oldCache = registryCacheMap.get(registryId);
        if (oldCache != null) {
            ChildData nodeData = oldCache.getCurrentData();
            byte[] datas = nodeData.getData();
            return JacksonUtil.toObject(datas, DiscoverRegistryConfig.class);
        }
        synchronized (registryCacheMap) {
            oldCache = registryCacheMap.get(registryId);
            if (oldCache != null) {
                ChildData nodeData = oldCache.getCurrentData();
                byte[] datas = nodeData.getData();
                return JacksonUtil.toObject(datas, DiscoverRegistryConfig.class);
            }
            CuratorFramework curatorFramework = connectionWrapper.getConnection(CuratorFramework.class);
            try {
                Stat stat = curatorFramework.checkExists().forPath(nodePath);
                if (stat == null) {
                    logger.error("could not get node {} stat", nodePath);
                    return null;
                }
                NodeCache nodeCache = new NodeCache(curatorFramework, nodePath);
                nodeCache.start();
                registryCacheMap.putIfAbsent(registryId, nodeCache);
                NodeCacheListener listener = new NodeCacheListener() {
                    public void nodeChanged() {
                        if (nodeCache.getCurrentData() != null) {
                            eventPost(new RegistryConfigLoadEvent(registryId));
                        } else {
                            eventPost(new RegistryConfigDeleteEvent(registryId));
                        }
                    }
                };
                nodeCache.getListenable().addListener(listener);
                return JacksonUtil.toObject(nodeCache.getCurrentData().getData(), DiscoverRegistryConfig.class);
            } catch (Exception e) {
                logger.error("{} config get error", nodePath, e);
            }
            return null;
        }
    }

    @Override
    public DiscoverConfig discoveryConfig(String dscrId) {
        ConnectionWrapper connectionWrapper = registryConnectionRepository.connection(RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
        if (connectionWrapper == null) {
            logger.warn("no registry connection");
            return null;
        }
        String nodePath = REGISTRY_PATH.concat("/discovery/").concat(dscrId);
        NodeCache oldCache = discoveryCacheMap.get(dscrId);
        if (oldCache != null) {
            ChildData nodeData = oldCache.getCurrentData();
            byte[] datas = nodeData.getData();
            return JacksonUtil.toObject(datas, DiscoverConfig.class);
        }
        synchronized (discoveryCacheMap) {
            oldCache = discoveryCacheMap.get(dscrId);
            if (oldCache != null) {
                ChildData nodeData = oldCache.getCurrentData();
                byte[] datas = nodeData.getData();
                return JacksonUtil.toObject(datas, DiscoverConfig.class);
            }
            CuratorFramework curatorFramework = connectionWrapper.getConnection(CuratorFramework.class);
            try {
                Stat stat = curatorFramework.checkExists().forPath(nodePath);
                if (stat == null) {
                    logger.error("could not get node {} stat", nodePath);
                    return null;
                }
                NodeCache nodeCache = new NodeCache(curatorFramework, nodePath);
                nodeCache.start();
                discoveryCacheMap.putIfAbsent(dscrId, nodeCache);
                NodeCacheListener listener = new NodeCacheListener() {
                    public void nodeChanged() {
                        if (nodeCache.getCurrentData() != null) {
                            eventPost(new DiscoverConfigLoadEvent(dscrId));
                        } else {
                            eventPost(new DiscoverConfigDeleteEvent(dscrId));
                        }
                    }
                };
                nodeCache.getListenable().addListener(listener);
                return JacksonUtil.toObject(nodeCache.getCurrentData().getData(), DiscoverConfig.class);
            } catch (Exception e) {
                logger.error("{} config get error", nodePath, e);
            }
            return null;
        }
    }

    @Override
    public DiscoverConfig discoveryConfigFromLoadBalance(String lbMark) {
        LoadbalanceInfo loadbalanceInfo = this.loadbalanceConfig(lbMark);
        if (loadbalanceInfo == null || loadbalanceInfo.getDscrEnable().equals(0) || StringUtils.isEmpty(loadbalanceInfo.getDscrId()))
            return null;
        return this.discoveryConfig(loadbalanceInfo.getDscrId());
    }

    @Override
    public LoadbalanceInfo loadbalanceConfig(String lbMark) {
        ConnectionWrapper connectionWrapper = registryConnectionRepository.connection(RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
        if (connectionWrapper == null) {
            logger.warn("no registry connection");
            return null;
        }
        String nodePath = REGISTRY_PATH.concat("/loadbalance/").concat(lbMark);
        NodeCache oldCache = loadbalanceCacheMap.get(lbMark);
        if (oldCache != null) {
            ChildData nodeData = oldCache.getCurrentData();
            byte[] datas = nodeData.getData();
            return JacksonUtil.toObject(datas, LoadbalanceInfo.class);
        }
        synchronized (loadbalanceCacheMap) {
            oldCache = loadbalanceCacheMap.get(lbMark);
            if (oldCache != null) {
                ChildData nodeData = oldCache.getCurrentData();
                byte[] datas = nodeData.getData();
                return JacksonUtil.toObject(datas, LoadbalanceInfo.class);
            }
            CuratorFramework curatorFramework = connectionWrapper.getConnection(CuratorFramework.class);
            try {
                Stat stat = curatorFramework.checkExists().forPath(nodePath);
                if (stat == null) {
                    logger.error("could not get node {} stat", nodePath);
                    return null;
                }
                NodeCache nodeCache = new NodeCache(curatorFramework, nodePath);
                nodeCache.start();
                registryCacheMap.putIfAbsent(lbMark, nodeCache);
                NodeCacheListener listener = new NodeCacheListener() {
                    public void nodeChanged() {
                        if (nodeCache.getCurrentData() != null) {
                            eventPost(new LoadBalanceConfigLoadEvent(lbMark));
                        } else {
                            eventPost(new LoadBalanceConfigDeleteEvent(lbMark));
                        }
                    }
                };
                nodeCache.getListenable().addListener(listener);
                return JacksonUtil.toObject(nodeCache.getCurrentData().getData(), LoadbalanceInfo.class);
            } catch (Exception e) {
                logger.error("{} config get error", nodePath, e);
            }
            return null;
        }
    }

    @Override
    public AbstractConfiguration getConfiguration() {
        return this;
    }
}
