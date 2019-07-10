package com.doctorwork.com.sword.gateway.registry;

import com.doctorwork.com.sword.gateway.registry.config.RegistryConfig;
import com.doctorwork.com.sword.gateway.registry.wrapper.ConnectionWrapper;
import com.doctorwork.sword.gateway.common.event.AbstractEvent;
import com.doctorwork.sword.gateway.common.event.EventPost;
import com.doctorwork.sword.gateway.common.event.RegistryLoadEvent;
import com.doctorwork.sword.gateway.dal.model.DiscoverRegistryConfig;
import com.doctorwork.sword.gateway.service.GatewayDiscoveryConnectionService;
import com.google.common.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:03 2019/7/4
 * @Modified By:
 */
public class RegistryConnectionRepositoryManager implements IRegistryConnectionRepository, EventPost {
    protected static final Logger logger = LoggerFactory.getLogger(RegistryConnectionRepositoryManager.class);

    private final Map<String, ConnectionWrapper> connectionWrapperMap = new ConcurrentHashMap<>();
    public static final String DEFAULT_ZOOKEEPER = "default";
    private AtomicBoolean updateFlag = new AtomicBoolean(false);

    private GatewayDiscoveryConnectionService gatewayDiscoveryConnectionService;
    private RegistryConfig defaultRegistryConfig;
    private EventBus eventBus;

    public RegistryConnectionRepositoryManager(GatewayDiscoveryConnectionService gatewayDiscoveryConnectionService, RegistryConfig defaultRegistryConfig, EventBus eventBus) {
        this.gatewayDiscoveryConnectionService = gatewayDiscoveryConnectionService;
        this.defaultRegistryConfig = defaultRegistryConfig;
        this.eventBus = eventBus;
    }

    @Override
    public ConnectionWrapper connection(String registryId) {
        return connectionWrapperMap.get(registryId);
    }

    @Override
    //重载此处 需要将对应的发现进行重载 然后进行关闭操作
    public void connectionLoad(String registryId) throws IOException {
        RegistryConfig registryConfig;
        if (registryId.equals(DEFAULT_ZOOKEEPER)) {
            registryConfig = defaultRegistryConfig;
            if (registryConfig == null) {
                logger.error("no service discovery connection config for {}", registryId);
                return;
            }
        } else {
            DiscoverRegistryConfig discoverRegistryConfig = gatewayDiscoveryConnectionService.get(registryId);
            if (discoverRegistryConfig == null) {
                logger.error("no service discovery connection config for {}", registryId);
                return;
            }
            registryConfig = RegistryConfig.build(discoverRegistryConfig);
        }
        ConnectionWrapper connectionWrapper = registryConfig.buidRegistry();
        if (connectionWrapper == null) {
            logger.error("service discovery connection create faild for {}", registryId);
            return;
        }
        ConnectionWrapper old = null;
        if (!updateFlag.compareAndSet(false, true)) {
            return;
        }
        try {
            old = connectionWrapperMap.get(registryId);
            connectionWrapperMap.put(registryId, connectionWrapper);
            Boolean isolde = old != null;
            eventPost(new RegistryLoadEvent(registryId, isolde));
            if (isolde) {
                old.close();
            }
        } catch (Exception e) {
            logger.info("error happened while connectionLoad regitry for {}", registryId, e);
        } finally {
            updateFlag.set(false);
        }
    }

    @Override
    public void connectionClose(String registryId) throws IOException {
        ConnectionWrapper wrapper = connectionWrapperMap.get(registryId);
        connectionWrapperMap.remove(registryId);
        wrapper.close();
    }

    @Override
    public void eventPost(AbstractEvent event) {
        if (eventBus != null)
            eventBus.post(event);
    }
}
