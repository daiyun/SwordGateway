package com.doctorwork.sword.gateway.admin;

import com.doctorwork.sword.gateway.common.BusinessException;
import com.doctorwork.sword.gateway.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:53 2019/7/25
 * @Modified By:
 */
@ControllerAdvice
public class ExceptionInterceptor {
    /**
     * 日志对象.
     */
    protected static final Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);

    @ExceptionHandler
    @ResponseBody // 在返回自定义相应类的情况下必须有，这是@ControllerAdvice注解的规定
    public Result RuntineExceptionHandler(Throwable e, HttpServletResponse response) {
        if (e instanceof BusinessException) {
            BusinessException ex = (BusinessException) e;
            logger.error("[BusinessException], code:[{}], message:[{}]", ex.getCode(), ex.getMessage());
            return Result.error(ex.getCode(), ex.getMessage());
        }
        logger.error("Unknown error", e);
        return Result.error("unknown error");

    }
}
