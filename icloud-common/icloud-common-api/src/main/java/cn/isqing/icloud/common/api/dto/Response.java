package cn.isqing.icloud.common.api.dto;

import cn.isqing.icloud.common.api.enums.ResCodeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应基类
 *
 * @author songqingwei
 * @version 1.0
 **/
@Data
public class Response<T> implements Serializable {

    private String code;

    private String msg;

    private T data;

    public static final Response<Object> SUCCESS = new Response<>(ResCodeEnum.SUCCESS.getCode(), "成功");

    public static final Response<Object> ERROR = new Response<>(ResCodeEnum.ERROR.getCode(), "异常");

    public Response(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Response(String code) {
        this.code = code;
    }

    public Response(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> Response<T> info(String code, String msg) {
        return new Response<>(code, msg);
    }

    public static <T> Response<T> info(String code, String msg, T data) {
        return new Response<>(code, msg, data);
    }

    public static <T> Response<T> error(String msg) {
        return new Response<>(ResCodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Response<T> error(String msg, T data) {
        return new Response<>(ResCodeEnum.ERROR.getCode(), msg, data);
    }

    public static <R> Response<R> cleanData(Response res) {
        res.setData(null);
        return res;
    }

    public static <T> Response<T> success(String msg) {
        return new Response<>(ResCodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Response<T> success(String msg, T data) {
        return new Response<>(ResCodeEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(ResCodeEnum.SUCCESS.getCode(), ResCodeEnum.SUCCESS.getMsg(), data);
    }

    public boolean isSuccess() {
        return ResCodeEnum.SUCCESS.getCode().equals(this.code);
    }

}
