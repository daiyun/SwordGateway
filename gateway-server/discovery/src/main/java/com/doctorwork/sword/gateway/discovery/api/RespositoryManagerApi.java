package com.doctorwork.sword.gateway.discovery.api;

import com.doctorwork.sword.gateway.discovery.IDiscoveryConnectionRepository;
import com.doctorwork.sword.gateway.discovery.IDiscoveryRepository;
import com.doctorwork.sword.gateway.discovery.ServiceWrapper;
import com.doctorwork.sword.gateway.discovery.connection.DiscoveryConnectionWrapper;
import com.doctorwork.sword.gateway.discovery.connection.ServiceDiscoveryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:00 2019/7/4
 * @Modified By:
 */
public class RespositoryManagerApi implements IRespositoryManagerApi {
    private static final Logger logger = LoggerFactory.getLogger(RespositoryManagerApi.class);
    private static ExecutorService executors = Executors.newSingleThreadExecutor();

    private IDiscoveryConnectionRepository discoveryConnectionRepository;
    private IDiscoveryRepository discoveryRepository;

    public RespositoryManagerApi(IDiscoveryConnectionRepository discoveryConnectionRepository, IDiscoveryRepository discoveryRepository) {
        if (discoveryConnectionRepository == null || discoveryRepository == null)
            throw new RuntimeException("discoveryConnectionRepository and discoveryRepository must not be null");
        this.discoveryConnectionRepository = discoveryConnectionRepository;
        this.discoveryRepository = discoveryRepository;
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> executors.shutdown()));
    }

    public DiscoveryConnectionWrapper connection(String registryId) {
        return discoveryConnectionRepository.connection(registryId);
    }

    @Override
    public void connectionLoad(String registryId) {
        executors.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    discoveryConnectionRepository.connectionLoad(registryId, discoveryRepository);
                } catch (IOException e) {
                    logger.error("error happened while load connection for {}", registryId, e);
                }
            }
        });
    }

    @Override
    public void connectionClose(String registryId) {
        executors.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    discoveryConnectionRepository.connectionClose(registryId);
                } catch (IOException e) {
                    logger.error("error happened while close connection for {}", registryId, e);
                }
            }
        });
    }

    @Override
    public ServiceWrapper serviceWrapper(String serviceId) {
        return discoveryRepository.serviceWrapper(serviceId);
    }

    @Override
    public ServiceDiscoveryWrapper serviceDisovery(ServiceWrapper serviceWrapper) {
        return discoveryRepository.serviceDisovery(serviceWrapper);
    }

    @Override
    public void loadService(String serviceId) {
        executors.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    discoveryRepository.loadService(serviceId, null);
                } catch (Exception e) {
                    logger.error("error happened while load service for {}", serviceId, e);
                }
            }
        });
    }

    @Override
    public void loadDiscovery(String dscrId) {
        executors.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    discoveryRepository.loadDiscovery(dscrId, null);
                } catch (Exception e) {
                    logger.error("error happened while load discovery for {}", dscrId, e);
                }
            }
        });
    }

    @Override
    public void loadRegistry(String registryId) {
        executors.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    discoveryRepository.loadRegistry(registryId);
                } catch (Exception e) {
                    logger.error("error happened while load registry for {}", registryId, e);
                }
            }
        });
    }

    @Override
    public void serviceDelete(String serviceId) {
        executors.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    discoveryRepository.serviceDelete(serviceId);
                } catch (Exception e) {
                    logger.error("error happened while delete service for {}", serviceId, e);
                }
            }
        });
    }
}
