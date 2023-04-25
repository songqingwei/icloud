package cn.isqing.icloud.common.utils.enums.status;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

public enum SubFlowStatusEnum {

    DEFAULT(0,"初始化"),
    PENDING(1,"待处理"),
    DOING(2,"进行中"),
    FAILED(3,"失败"),
    SUCCESS(4,"成功"),
    CANCLE(5,"取消"),
    ;

    @Getter
    private int code;
    @Getter
    private String note;

    SubFlowStatusEnum(int code, String note) {
        this.code = code;
        this.note = note;
    }

    @JsonCreator
    public static SubFlowStatusEnum byCode(int code) {
        Optional<SubFlowStatusEnum> first = Arrays.stream(values()).filter(e -> e.getCode() == code).findFirst();
        return first.orElse(null);
    }
}
