package com.doctorwork.sword.gateway.config;

import com.doctorwork.com.sword.gateway.registry.IRegistryConnectionRepository;
import com.doctorwork.com.sword.gateway.registry.RegistryConnectionRepositoryManager;
import com.doctorwork.com.sword.gateway.registry.wrapper.ConnectionWrapper;
import com.doctorwork.sword.gateway.common.JacksonUtil;
import com.doctorwork.sword.gateway.common.config.ConnectionInfo;
import com.doctorwork.sword.gateway.common.config.DiscoveryInfo;
import com.doctorwork.sword.gateway.common.config.LoadBalancerInfo;
import com.doctorwork.sword.gateway.common.config.LoadBalancerServer;
import com.doctorwork.sword.gateway.common.event.*;
import com.doctorwork.sword.gateway.common.listener.EventListener;
import com.doctorwork.sword.gateway.discovery.common.util.StringUtils;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
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
    private final Map<String, NodeCache> discoveryCacheMap = new HashMap<>();
    private final Map<String, NodeCache> registryCacheMap = new HashMap<>();
    private Map<String, NodeCache> loadbalanceCacheMap = new HashMap<>();
    private final AtomicBoolean init = new AtomicBoolean(false);
    private StampedLock stampedLock = new StampedLock();


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
    @Subscribe
    public void handleEvent(AbstractEvent event) {
        if (event instanceof RegistryLoadEvent) {
            logger.info("handle event RegistryLoadEvent");
            RegistryLoadEvent registryLoadEvent = (RegistryLoadEvent) event;
            String registryId = registryLoadEvent.getRegistryId();
            if (RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER.equals(registryId) && init.get()) {
                long stamp = stampedLock.writeLock();
                try {
                    //reload default zookeeper,reload node
                    for (Map.Entry<String, NodeCache> registryCacheEntry : registryCacheMap.entrySet()) {
                        String cacheEntryKey = registryCacheEntry.getKey();
                        loadRegistryConfig(cacheEntryKey, true);
                    }
                    for (Map.Entry<String, NodeCache> discoveryCacheEntry : discoveryCacheMap.entrySet()) {
                        String cacheEntryKey = discoveryCacheEntry.getKey();
                        loadDiscoveryConfig(cacheEntryKey, true);
                    }
                    for (Map.Entry<String, NodeCache> loadbalanceCacheEntry : loadbalanceCacheMap.entrySet()) {
                        String cacheEntryKey = loadbalanceCacheEntry.getKey();
                        loadLoadbalanceConfig(cacheEntryKey, true);
                    }
                } finally {
                    stampedLock.unlockWrite(stamp);
                }

            }
        } else if (event instanceof RegistryConfigDeleteEvent) {
            logger.info("handle event RegistryConfigDeleteEvent");
            RegistryConfigDeleteEvent deleteEvent = (RegistryConfigDeleteEvent) event;
            long stamp = stampedLock.writeLock();
            try {
                String registryId = deleteEvent.getRegistryId();
                NodeCache nodeCache = registryCacheMap.remove(registryId);
                if (nodeCache != null)
                    nodeCache.close();
            } catch (IOException e) {
                logger.error("error happened while handle RegistryConfigDeleteEvent for {}", deleteEvent.getRegistryId(), e);
            } finally {
                stampedLock.unlockWrite(stamp);
            }
        } else if (event instanceof DiscoverConfigDeleteEvent) {
            logger.info("handle event DiscoverConfigDeleteEvent");
            DiscoverConfigDeleteEvent deleteEvent = (DiscoverConfigDeleteEvent) event;
            long stamp = stampedLock.writeLock();
            try {
                String dscrId = deleteEvent.getDscrId();
                NodeCache nodeCache = discoveryCacheMap.remove(dscrId);
                if (nodeCache != null)
                    nodeCache.close();
            } catch (IOException e) {
                logger.error("error happened while handle DiscoverConfigDeleteEvent for {}", deleteEvent.getDscrId(), e);
            } finally {
                stampedLock.unlockWrite(stamp);
            }
        } else if (event instanceof LoadBalanceConfigDeleteEvent) {
            logger.info("handle event LoadBalanceConfigDeleteEvent");
            LoadBalanceConfigDeleteEvent deleteEvent = (LoadBalanceConfigDeleteEvent) event;
            long stamp = stampedLock.writeLock();
            try {
                String lbMark = deleteEvent.getLbMark();
                NodeCache nodeCache = loadbalanceCacheMap.remove(lbMark);
                if (nodeCache != null)
                    nodeCache.close();
            } catch (IOException e) {
                logger.error("error happened while handle LoadBalanceConfigDeleteEvent for {}", deleteEvent.getLbMark(), e);
            } finally {
                stampedLock.unlockWrite(stamp);
            }
        }
    }

    @Override
    public ConnectionInfo connectionConfig(String registryId) {
        long stamp = stampedLock.tryOptimisticRead();
        byte[] datas = null;
        NodeCache nodeCache = registryCacheMap.get(registryId);
        if (nodeCache != null) {
            ChildData nodeData = nodeCache.getCurrentData();
            datas = nodeData.getData();
        } else {
            if (loadRegistryConfig(registryId, false)) {
                nodeCache = registryCacheMap.get(registryId);
                if (nodeCache == null)
                    return null;
                ChildData nodeData = nodeCache.getCurrentData();
                datas = nodeData.getData();
            }
        }
        if (!stampedLock.validate(stamp)) {
            stampedLock.readLock();
            try {
                nodeCache = registryCacheMap.get(registryId);
                if (nodeCache == null)
                    return null;
                ChildData nodeData = nodeCache.getCurrentData();
                datas = nodeData.getData();
                return JacksonUtil.toObject(datas, ConnectionInfo.class);
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        if (datas != null) {
            return JacksonUtil.toObject(datas, ConnectionInfo.class);
        }
        return null;
    }

    private synchronized boolean loadRegistryConfig(String registryId, boolean loadForce) {
        ConnectionWrapper connectionWrapper = registryConnectionRepository.connection(RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
        if (connectionWrapper == null) {
            logger.warn("no registry connection");
            return false;
        }
        String nodePath = REGISTRY_PATH.concat("/registry/").concat(registryId);
        NodeCache oldCache = registryCacheMap.get(registryId);
        if (oldCache != null && !loadForce) {
            return true;
        }
        CuratorFramework curatorFramework = connectionWrapper.getConnection(CuratorFramework.class);
        try {
            Stat stat = curatorFramework.checkExists().forPath(nodePath);
            if (stat == null) {
                logger.error("could not get node {} stat", nodePath);
                return false;
            }
            NodeCache nodeCache = new NodeCache(curatorFramework, nodePath);
            nodeCache.start();
            NodeCacheListener listener = new NodeCacheListener() {
                public void nodeChanged() {
                    ChildData nodeData = nodeCache.getCurrentData();
                    if (nodeData != null) {
                        eventPost(new RegistryConfigLoadEvent(registryId, JacksonUtil.toObject(nodeData.getData(), ConnectionInfo.class)));
                    } else {
                        eventPost(new RegistryConfigDeleteEvent(registryId));
                    }
                }
            };
            nodeCache.getListenable().addListener(listener);
            registryCacheMap.put(registryId, nodeCache);
            if (oldCache != null) {
                oldCache.close();
            }
            return true;
        } catch (Exception e) {
            logger.error("{} config get error", nodePath, e);
        }
        return false;
    }

    @Override
    public DiscoveryInfo discoveryConfig(String dscrId) {
        long stamp = stampedLock.tryOptimisticRead();
        byte[] datas = null;
        NodeCache nodeCache = discoveryCacheMap.get(dscrId);
        if (nodeCache != null) {
            ChildData nodeData = nodeCache.getCurrentData();
            datas = nodeData.getData();
        } else {
            if (loadDiscoveryConfig(dscrId, false)) {
                nodeCache = discoveryCacheMap.get(dscrId);
                if (nodeCache == null)
                    return null;
                ChildData nodeData = nodeCache.getCurrentData();
                datas = nodeData.getData();
            }
        }
        if (!stampedLock.validate(stamp)) {
            stampedLock.readLock();
            try {
                nodeCache = discoveryCacheMap.get(dscrId);
                if (nodeCache == null)
                    return null;
                ChildData nodeData = nodeCache.getCurrentData();
                datas = nodeData.getData();
                return JacksonUtil.toObject(datas, DiscoveryInfo.class);
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        if (datas != null) {
            return JacksonUtil.toObject(datas, DiscoveryInfo.class);
        }
        return null;
    }

    private synchronized boolean loadDiscoveryConfig(String dscrId, boolean loadForce) {
        ConnectionWrapper connectionWrapper = registryConnectionRepository.connection(RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
        if (connectionWrapper == null) {
            logger.warn("no registry connection");
            return false;
        }
        String nodePath = REGISTRY_PATH.concat("/discovery/").concat(dscrId);
        NodeCache oldCache = discoveryCacheMap.get(dscrId);
        if (oldCache != null && !loadForce) {
            return true;
        }
        CuratorFramework curatorFramework = connectionWrapper.getConnection(CuratorFramework.class);
        try {
            Stat stat = curatorFramework.checkExists().forPath(nodePath);
            if (stat == null) {
                logger.error("could not get node {} stat", nodePath);
                return false;
            }
            NodeCache nodeCache = new NodeCache(curatorFramework, nodePath);
            nodeCache.start();
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
            discoveryCacheMap.put(dscrId, nodeCache);
            if (oldCache != null) {
                oldCache.close();
            }
            return true;
        } catch (Exception e) {
            logger.error("{} config get error", nodePath, e);
        }
        return false;
    }

    @Override
    public DiscoveryInfo discoveryConfigFromLoadBalance(String lbMark) {
        LoadBalancerInfo loadbalanceInfo = this.loadbalanceConfig(lbMark);
        if (loadbalanceInfo == null || loadbalanceInfo.getDscrEnable().equals(0) || StringUtils.isEmpty(loadbalanceInfo.getDiscoveryId()))
            return null;
        return this.discoveryConfig(loadbalanceInfo.getId());
    }

    @Override
    public LoadBalancerInfo loadbalanceConfig(String lbMark) {
        long stamp = stampedLock.tryOptimisticRead();
        byte[] datas = null;
        NodeCache nodeCache = loadbalanceCacheMap.get(lbMark);
        if (nodeCache != null) {
            ChildData nodeData = nodeCache.getCurrentData();
            datas = nodeData.getData();
        } else {
            if (loadLoadbalanceConfig(lbMark, false)) {
                nodeCache = loadbalanceCacheMap.get(lbMark);
                if (nodeCache == null)
                    return null;
                ChildData nodeData = nodeCache.getCurrentData();
                datas = nodeData.getData();
            }
        }
        if (!stampedLock.validate(stamp)) {
            stampedLock.readLock();
            try {
                nodeCache = loadbalanceCacheMap.get(lbMark);
                if (nodeCache == null)
                    return null;
                ChildData nodeData = nodeCache.getCurrentData();
                datas = nodeData.getData();
                return JacksonUtil.toObject(datas, LoadBalancerInfo.class);
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        if (datas != null) {
            return JacksonUtil.toObject(datas, LoadBalancerInfo.class);
        }
        return null;
    }

    private synchronized boolean loadLoadbalanceConfig(String lbMark, boolean loadForce) {
        ConnectionWrapper connectionWrapper = registryConnectionRepository.connection(RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
        if (connectionWrapper == null) {
            logger.warn("no registry connection");
            return false;
        }
        String nodePath = REGISTRY_PATH.concat("/loadbalance/").concat(lbMark);
        NodeCache oldCache = loadbalanceCacheMap.get(lbMark);
        if (oldCache != null && !loadForce) {
            return true;
        }
        CuratorFramework curatorFramework = connectionWrapper.getConnection(CuratorFramework.class);
        try {
            Stat stat = curatorFramework.checkExists().forPath(nodePath);
            if (stat == null) {
                logger.error("could not get node {} stat", nodePath);
                return false;
            }
            NodeCache nodeCache = new NodeCache(curatorFramework, nodePath);
            nodeCache.start();
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
            loadbalanceCacheMap.put(lbMark, nodeCache);
            if (oldCache != null) {
                oldCache.close();
            }
            return true;
        } catch (Exception e) {
            logger.error("{} config get error", nodePath, e);
        }
        return false;
    }

    @Override
    public Collection<LoadBalancerServer> loadbalanceServer(String lbMark) {
        ConnectionWrapper connectionWrapper = registryConnectionRepository.connection(RegistryConnectionRepositoryManager.DEFAULT_ZOOKEEPER);
        if (connectionWrapper == null) {
            logger.warn("no registry connection");
            return Collections.emptyList();
        }
        String nodePath = REGISTRY_PATH.concat("/loadbalance-server/").concat(lbMark);
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
    public AbstractConfiguration getConfiguration() {
        return this;
    }
}
