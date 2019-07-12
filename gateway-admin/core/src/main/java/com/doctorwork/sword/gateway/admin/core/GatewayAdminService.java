package com.doctorwork.sword.gateway.admin.core;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:59 2019/7/12
 * @Modified By:
 */
public interface GatewayAdminService {
    void publishLoadBalanceConfig(String lbMark) throws Exception;

    void publishLoadBalanceServer(String lbMark) throws Exception;

    void publishDiscoveryConfig(String dscrId) throws Exception;

    void publishRegistryConfig(String registryId) throws Exception;
}
