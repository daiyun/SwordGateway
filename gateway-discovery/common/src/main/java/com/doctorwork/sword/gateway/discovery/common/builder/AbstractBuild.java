package com.doctorwork.sword.gateway.discovery.common.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author:czq
 * @Description:
 * @Date: 11:23 2019/6/3
 * @Modified By:
 */
public abstract class AbstractBuild<T> implements Builder<T> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
}
