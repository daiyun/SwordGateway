package com.doctorwork.com.sword.gateway.registry.wrapper;

import com.doctorwork.com.sword.gateway.registry.config.RegistryConfig;

import java.io.Closeable;
import java.io.IOException;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:35 2019/7/4
 * @Modified By:
 */
public class ConnectionWrapper implements Closeable {

    private final String id;

    private final Closeable t;

    private final RegistryConfig registryConfig;

    public ConnectionWrapper(String id, Object t, RegistryConfig registryConfig) {
        this.id = id;
        this.registryConfig = registryConfig;
        if (t == null)
            throw new RuntimeException("discovery connection must not be null");
        this.t = (Closeable) t;
    }

    public <R> R getConnection(Class<R> clazz) {
        return clazz.cast(t);
    }

    @Override
    public void close() throws IOException {
        t.close();
    }

    public String getId() {
        return id;
    }

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }
}
