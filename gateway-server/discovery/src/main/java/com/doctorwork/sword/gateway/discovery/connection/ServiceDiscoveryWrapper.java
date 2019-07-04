package com.doctorwork.sword.gateway.discovery.connection;

import java.io.Closeable;
import java.io.IOException;

/**
 * @Author:czq
 * @Description:
 * @Date: 12:36 2019/7/4
 * @Modified By:
 */
public class ServiceDiscoveryWrapper<T extends Closeable> implements Closeable {
    private final T t;
    private final String id;
    private final String connectionId;

    public ServiceDiscoveryWrapper(T t, String id, String connectionId) {
        this.id = id;
        this.connectionId = connectionId;
        if (t == null)
            throw new RuntimeException("service discovery must not be null");
        this.t = t;
    }

    public String getId() {
        return id;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public T getServiceDiscovery() {
        return t;
    }

    @Override
    public void close() throws IOException {
        t.close();
    }
}
