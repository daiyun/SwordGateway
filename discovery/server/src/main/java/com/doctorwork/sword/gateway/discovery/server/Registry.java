package com.doctorwork.sword.gateway.discovery.server;

/**
 * @author chenzhiqiang
 * @date 2019/5/31
 */
public interface Registry<R extends Registration> {
    void register(R registration);

    void deregister(R registration);

    void start();

    void close();

    void setStatus(R registration, String status);

    <T> T getStatus(R registration);
}
