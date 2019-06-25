package com.doctorwork.sword.gateway.config;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:czq
 * @Description:
 * @Date: 19:03 2019/6/18
 * @Modified By:
 */
@Configuration
@RibbonClients(defaultConfiguration = LoadBalancerConfiguration.class)
public class LoadBalancerClientAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public LoadBalancerClient loadBalancerClient(SpringClientFactory springClientFactory) {
        return new RibbonLoadBalancerClient(springClientFactory) {
            @Override
            protected Server getServer(String serviceId) {
                ILoadBalancer loadBalancer = this.getLoadBalancer(serviceId);
                return loadBalancer == null ? null : chooseServerByServiceIdOrDefault(loadBalancer, serviceId);
            }

            private Server chooseServerByServiceIdOrDefault(ILoadBalancer loadBalancer, String serviceId) {
                Server server = loadBalancer.chooseServer(serviceId);
                return server != null ? server : loadBalancer.chooseServer("default");
            }
        };
    }
}
