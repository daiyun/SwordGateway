package com.doctorwork.sword.gateway.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Set;

/**
 * @Author:czq
 * @Description:
 * @Date: 18:20 2019/6/20
 * @Modified By:
 */
public class JacksonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    private JacksonUtil() {
    }

    private static final class HOLDER {
        private static final ObjectMapper INSTANT = new ObjectMapper();

        static {
            INSTANT.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
    }

    public static ObjectMapper getInstance() {
        return HOLDER.INSTANT;
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json))
            return null;
        try {
            return getInstance().readValue(json, clazz);
        } catch (IOException e) {
            logger.error("json 解析异常", e);
        }
        return null;
    }

    public static <T> T toSubTypeObject(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json))
            return null;
        Reflections reflections = new Reflections(clazz.getPackage().getName());
        Set<Class<? extends T>> classSet = reflections.getSubTypesOf(clazz);
        ObjectMapper mapper = new ObjectMapper();
        classSet.stream().forEach(c -> mapper.registerSubtypes(c));
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("json 解析异常", e);
        }
        return null;
    }

    public static <T> String toJSon(T t) {
        if (t == null)
            return null;
        try {
            return getInstance().writeValueAsString(t);
        } catch (JsonProcessingException e) {
            logger.error("json 序列化异常", e);
        }
        return null;
    }

}
