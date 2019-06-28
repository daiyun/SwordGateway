package com.doctorwork.sword.gateway.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Author:czq
 * @Description:
 * @Date: 18:08 2019/6/28
 * @Modified By:
 */
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanId) {
        return (T) applicationContext.getBean(beanId);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return (T) applicationContext.getBean(requiredType);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }
}
