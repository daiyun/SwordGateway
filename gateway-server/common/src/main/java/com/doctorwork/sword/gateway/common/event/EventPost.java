package com.doctorwork.sword.gateway.common.event;

import com.doctorwork.sword.gateway.common.event.AbstractEvent;

/**
 * @author chenzhiqiang
 * @date 2019/7/8
 */
public interface EventPost {
    void eventPost(AbstractEvent event);
}
