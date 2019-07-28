package com.doctorwork.sword.gateway.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;

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
            INSTANT.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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

    public static <T> T toObject(byte[] bytes, Class<T> clazz) {
        if (bytes == null)
            return null;
        try {
            return getInstance().readValue(bytes, clazz);
        } catch (IOException e) {
            logger.error("json 解析异常", e);
        }
        return null;
    }

    public static <T> T toObject(byte[] bytes, JavaType type) {
        if (bytes == null)
            return null;
        try {
            return getInstance().readValue(bytes, type);
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

    public static <T> byte[] toBytes(T t) {
        if (t == null)
            return null;
        try {
            return getInstance().writeValueAsBytes(t);
        } catch (JsonProcessingException e) {
            logger.error("json 序列化异常", e);
        }
        return null;
    }

    public static JavaType getType(Class clzz){
        return getInstance().getTypeFactory().constructType(clzz);
    }
}
