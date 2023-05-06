package cn.isqing.icloud.common.api.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author songqingwei
 * @version 1.0
 **/
@Getter
public enum ResCodeEnum {
    SUCCESS("000000", "成功"),
    FAILED("000001", "失败"),
    ERROR("000002", "异常"),
    SYSTEM_ERROR("000003", "系统异常"),
    BUSI_ERROR("000004", "业务异常"),
    HANDLED("000005", "已处理"),
    REJECT("000006", "拒绝处理"),
    VALIDATE_ERROR("000007", "参数校验未通过"),
    RQ("000008", "重复请求"),
    NOTFIND("000009", "未获取到相关记录"),
    CANCEL("000010", "取消"),
    TIMEOUT("000011", "超时"),

    ;

    private String code;
    private String msg;

    ResCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ResCodeEnum get(String code) {
        Optional<ResCodeEnum> first =
                Arrays.stream(ResCodeEnum.values()).filter(e -> e.getCode().equals(code)).findFirst();
        return first.isPresent() ? first.get() : null;
    }

}
