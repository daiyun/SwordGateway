package com.doctorwork.com.sword.gateway.registry;

import com.doctorwork.com.sword.gateway.registry.config.RegistryConfig;
import com.doctorwork.com.sword.gateway.registry.wrapper.ConnectionWrapper;
import com.doctorwork.sword.gateway.common.config.ConnectionInfo;
import com.doctorwork.sword.gateway.common.config.IConnectionConfigRepository;
import com.doctorwork.sword.gateway.common.event.*;
import com.doctorwork.sword.gateway.common.listener.EventListener;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:03 2019/7/4
 * @Modified By:
 */
public class RegistryConnectionRepositoryManager implements IRegistryConnectionRepository, EventPost, EventListener<RegistryConfigEvent> {
    protected static final Logger logger = LoggerFactory.getLogger(RegistryConnectionRepositoryManager.class);

    private final Map<String, ConnectionWrapper> connectionWrapperMap = new ConcurrentHashMap<>();
    public static final String DEFAULT_ZOOKEEPER = "default";

    private IConnectionConfigRepository connectionConfigRepository;
    private RegistryConfig defaultRegistryConfig;
    private EventBus eventBus;

    public RegistryConnectionRepositoryManager(RegistryConfig defaultRegistryConfig, EventBus eventBus) {
        this.defaultRegistryConfig = defaultRegistryConfig;
        this.eventBus = eventBus;
        register(this.eventBus);
    }

    @Override
    public ConnectionWrapper connection(String registryId) {
        return connectionWrapperMap.get(registryId);
    }

    @Override
    //重载此处 需要将对应的发现进行重载 然后进行关闭操作
    public synchronized void connectionLoad(String registryId) {
        RegistryConfig registryConfig;
        if (registryId.equals(DEFAULT_ZOOKEEPER)) {
            registryConfig = defaultRegistryConfig;
            if (registryConfig == null) {
                logger.error("no service discovery connection config for {}", registryId);
                return;
            }
        } else {
            ConnectionInfo connectionInfo = connectionConfigRepository.connectionConfig(registryId);
            if (connectionInfo == null) {
                logger.error("no service discovery connection config for {}", registryId);
                return;
            }
            registryConfig = RegistryConfig.build(connectionInfo);
        }
        ConnectionWrapper connectionWrapper = registryConfig.buidRegistry();
        if (connectionWrapper == null) {
            logger.error("service discovery connection create faild for {}", registryId);
            return;
        }
        ConnectionWrapper old;
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

    @Override
    @Subscribe
    public void handleEvent(RegistryConfigEvent event) {
        if (event instanceof RegistryConfigLoadEvent) {
            RegistryConfigLoadEvent loadEvent = (RegistryConfigLoadEvent) event;
            String registryId = loadEvent.getRegistryId();
            logger.info("[RegistryConfigLoadEvent]handle event for {} version {}", registryId, loadEvent.getVersion());
            ConnectionWrapper connectionWrapper = connectionWrapperMap.get(registryId);
            if (connectionWrapper != null && connectionWrapper.versionValidate(loadEvent.getVersion())) {
                logger.info("[RegistryConfigLoadEvent]event for {}, version validate,no need to reload", registryId);
                return;
            }
            this.connectionLoad(registryId);
        } else if (event instanceof RegistryConfigDeleteEvent) {
            RegistryConfigDeleteEvent deleteEvent = (RegistryConfigDeleteEvent) event;
            logger.info("[RegistryConfigDeleteEvent]handle event for {}, but do nothing", deleteEvent.getRegistryId());
        }
    }

    @Override
    public void setConnectionConfig(IConnectionConfigRepository connectionConfigRepository) {
        this.connectionConfigRepository = connectionConfigRepository;
    }
}
