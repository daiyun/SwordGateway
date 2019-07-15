package com.doctorwork.com.sword.gateway.registry.wrapper;

import org.apache.curator.utils.CloseableUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:35 2019/7/4
 * @Modified By:
 */
public class ConnectionWrapper implements Closeable {

    private final String id;

    private final Closeable t;

    private final Integer version;

    public ConnectionWrapper(String id, Object t, Integer version) {
        this.id = id;
        this.version = version;
        if (t == null)
            throw new RuntimeException("discovery connection must not be null");
        this.t = (Closeable) t;
    }

    public <R> R getConnection(Class<R> clazz) {
        return clazz.cast(t);
    }

    @Override
    public void close() {
        CloseableUtils.closeQuietly(t);
    }

    public String getId() {
        return id;
    }

    public boolean versionValidate(Integer version) {
        return Objects.equals(this.version, version);
    }
}
