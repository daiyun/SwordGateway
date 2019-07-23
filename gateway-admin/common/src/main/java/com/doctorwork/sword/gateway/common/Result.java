package com.doctorwork.sword.gateway.common;

/**
 * @Author:czq
 * @Description:
 * @Date: 10:39 2019/7/23
 * @Modified By:
 */
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> result(T data) {
        Result<T> result = new Result<>();
        result.setCode(0);
        result.setData(data);
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
