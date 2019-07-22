package com.doctorwork.sword.gateway.common.event;

/**
 * @Author:czq
 * @Description:
 * @Date: 16:28 2019/7/22
 * @Modified By:
 */
public abstract class RouteEvent extends AbstractEvent {
    private String routeMark;

    public RouteEvent(String routeMark) {
        this.routeMark = routeMark;
    }

    public String getRouteMark() {
        return routeMark;
    }
}