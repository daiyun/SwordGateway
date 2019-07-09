package com.doctorwork.com.sword.gateway.registry.wrapper;

import java.io.Closeable;
import java.io.IOException;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:35 2019/7/4
 * @Modified By:
 */
public class DiscoveryConnectionWrapper<T extends Closeable> implements Closeable {

    private final String id;

    private final T t;

    public DiscoveryConnectionWrapper(String id, T t) {
        this.id = id;
        if (t == null)
            throw new RuntimeException("discovery connection must not be null");
        this.t = t;
    }

    public T getConnection() {
        return t;
    }

    @Override
    public void close() throws IOException {
        t.close();
    }

    public String getId() {
        return id;
    }
}
