package com.doctorwork.sword.gateway.common.config;

import java.util.Collection;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:29 2019/7/9
 * @Modified By:
 */
public interface IDiscoveryConfigRepository {
    DiscoveryInfo discoveryConfig(String dscrId);

    DiscoveryInfo discoveryConfigFromLoadBalance(String lbMark);

    Collection<DiscoveryInfo> all();
}
