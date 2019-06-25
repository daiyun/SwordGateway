package com.doctorwork.sword.gateway.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * ProjectName: server-gateway
 * Description: 动态加载路由规则-测试使用，正确的做法是改为定时任务去拉取配置信息
 * User: wangkai <wangkai@doctorwork.com>
 * Date: 2018/10/18
 * Time: 3:50 PM
 */
@Deprecated
public class RouteLoadRunner implements CommandLineRunner, ApplicationEventPublisherAware {

    private static final Logger logger = LoggerFactory.getLogger(RouteLoadRunner.class);

    private ApplicationEventPublisher publisher;

    @Override
    public void run(String... args) {
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }
}
