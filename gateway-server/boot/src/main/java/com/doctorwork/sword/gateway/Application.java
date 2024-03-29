package com.doctorwork.sword.gateway;

import com.doctorwork.sword.gateway.common.SpringContextUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

/**
 * @author wangkai
 */
@SpringBootApplication
@ImportResource(locations = {"classpath*:/spring/spring-*.xml"})
public class Application {
    /**
     * NettyServer
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public SpringContextUtils springContextUtils() {
        return new SpringContextUtils();
    }
}
