package com.doctorwork.sword.gateway.config;

/**
 * @Author:czq
 * @Description:
 * @Date: 14:40 2019/7/11
 * @Modified By:
 */
public class GatewayConfig {
    private boolean useRegistry = true;

    public boolean isUseRegistry() {
        return useRegistry;
    }

    public void setUseRegistry(boolean useRegistry) {
        this.useRegistry = useRegistry;
    }
}
