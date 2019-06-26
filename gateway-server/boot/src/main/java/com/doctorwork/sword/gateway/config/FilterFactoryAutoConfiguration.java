package com.doctorwork.sword.gateway.config;

import com.doctorwork.sword.gateway.filter.AccessLogGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:50 2019/6/26
 * @Modified By:
 */
@Configuration
public class FilterFactoryAutoConfiguration {
    @Bean
    public AccessLogGatewayFilterFactory accessLogGatewayFilterFactory() {
        return new AccessLogGatewayFilterFactory();
    }
}
