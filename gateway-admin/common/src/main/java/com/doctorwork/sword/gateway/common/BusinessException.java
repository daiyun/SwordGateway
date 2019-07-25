package com.doctorwork.sword.gateway.common;

/**
 * @Author:czq
 * @Description:
 * @Date: 17:55 2019/7/25
 * @Modified By:
 */
public class BusinessException extends Exception {
    private static final long serialVersionUID = 165367809284687797L;
    private int code;
    private String message;
    private String i18nMessage;

    public BusinessException(String message) {
        this.code = -2;//default business error
        this.message = message;
    }

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(int code, String message, String i18nMessage) {
        this.code = code;
        this.message = message;
        this.i18nMessage = i18nMessage;
    }

    public BusinessException(int code, String message, Object... args) {
        this.code = code;
        this.message = String.format(message, args);
    }

    public BusinessException(int code, String message, String i18nMessage, Object... args) {
        this.code = code;
        this.message = String.format(message, args);
        this.i18nMessage = String.format(i18nMessage, args);
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getI18nMessage() {
        return this.i18nMessage;
    }
}
