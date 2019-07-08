package com.doctorwork.sword.gateway.common.listener;

import com.doctorwork.sword.gateway.common.event.AbstractEvent;
import com.google.common.eventbus.EventBus;

/**
 * @author chenzhiqiang
 * @date 2019/7/8
 */
public interface EventListener<E extends AbstractEvent> {
    void handleEvent(E e);

    default void register(EventBus eventBus) {
        if (eventBus != null)
            eventBus.register(this);
    }
}
