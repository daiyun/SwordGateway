package com.doctorwork.sword.gateway.loadbalance;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:27 2019/7/3
 * @Modified By:
 */
public interface IDiscoveryRepository<T> {
    T get(String serviceId);

    void load(String serviceId);

    void delete(String serviceId);
}
