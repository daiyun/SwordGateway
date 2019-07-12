package com.doctorwork.sword.gateway.admin;

import com.doctorwork.sword.gateway.discovery.common.builder.CuratorBuilder;
import com.doctorwork.sword.gateway.discovery.common.builder.ZookeeperProperties;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:36 2019/7/12
 * @Modified By:
 */
@Configuration
public class CuratorConfiguration {

    @Bean
    public CuratorFramework curatorFramework(ZookeeperProperties zookeeperProperties) {
        CuratorBuilder curatorBuilder = new CuratorBuilder(zookeeperProperties);
        return curatorBuilder.build();
    }
}
