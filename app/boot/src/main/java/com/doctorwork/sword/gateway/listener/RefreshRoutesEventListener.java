package com.doctorwork.sword.gateway.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

/**
 * ProjectName: server-gateway
 * Description: 动态刷新路由规则监听器
 * User: wangkai <wangkai@doctorwork.com>
 * Date: 2018/10/18
 * Time: 4:49 PM
 * <p>
 * Spring容器中的事件监听器，与java中基本的事件监听器的定义相比，这里需要实现ApplicationListener接口
 * <p>
 * ApplicationListener接口虽然继承自EventListener，但扩展了EventListener
 * <p>
 * 它在接口声明中定义了onApplicationEvent的接口方法，而不像EventListener只作为标记性接口。
 */

@Service
public class RefreshRoutesEventListener implements ApplicationListener<RefreshRoutesEvent> {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(RefreshRoutesEventListener.class);

    @Override
    public void onApplicationEvent(RefreshRoutesEvent refreshRoutesEvent) {

        // 保存规则
        Object source = refreshRoutesEvent.getSource();
        if (source instanceof RouteDefinition) {

        }
        // todo 刷新规则 ，
        // 方式一，如果规则发生的改变，通过消息通知到网关服务，网关服务实时监听规则最新动向；
        // 方式二，admin管理缓存的后期更新，网关定时从缓存载入数据进行更新，时间边界的问题 ；
        // 方法三，二者结合composite

        // todo 路由规则加载完成mq通知到admin

    }
}
