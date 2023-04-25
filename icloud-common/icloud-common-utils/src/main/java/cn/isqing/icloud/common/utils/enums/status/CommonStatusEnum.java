package cn.isqing.icloud.common.utils.enums.status;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum CommonStatusEnum {

    DEFAULT(0,"初始化"),
    DOING(1,"进行中"),
    FAILED(2,"失败"),
    SUCCESS(3,"成功"),
    CANCLE(4,"取消"),
    ;

    @Getter
    private int code;
    @Getter
    private String note;

    CommonStatusEnum(int code,String note) {
        this.code = code;
        this.note = note;
    }

    @JsonCreator
    public static CommonStatusEnum byCode(int code) {
        Optional<CommonStatusEnum> first = Arrays.stream(values()).filter(e -> e.getCode() == code).findFirst();
        return first.orElse(null);
    }
}
