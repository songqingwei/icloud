package cn.isqing.icloud.common.utils.dto;

import cn.isqing.icloud.common.utils.enums.ResCodeEnum;
import lombok.Getter;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class BaseException extends RuntimeException {

    @Getter
    private String code;

    public BaseException(String msg) {
        super(msg);
        this.code = ResCodeEnum.ERROR.getCode();
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(int code, String message) {
        super(message);
        this.code = code+"";
    }
    public BaseException(String code, String message, Exception e) {
        super(message, e);
        this.code = code;
    }

    public BaseException(int code, String message, Exception e) {
        super(message, e);
        this.code = code+"";
    }
}
