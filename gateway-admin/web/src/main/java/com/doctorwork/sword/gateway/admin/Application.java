package com.doctorwork.sword.gateway.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @author wangkai
 */
@SpringBootApplication
@ImportResource(locations = {"classpath*:spring-*.xml"})
public class Application {
    /**
     * NettyServer
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
