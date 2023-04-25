package cn.isqing.icloud.starter.drools.common.enums;

import java.util.Arrays;

/**
 * 分配模式
 */
public enum OperatorType {

    GREATER(1, ">"),
    GREATER_EQ(2, ">="),
    LESS(3, "<"),
    LESS_EQ(4, "<="),
    EQ(5, "=="),
    CONTAINS(6, "contains"),
    NOT_CONTAINS(7, "not contains"),
    MEMBEROF(8, "memberof"),
    NOT_MEMBEROF(9, "not memberof"),
    MATCHES(10, "matches"),
    NOT_MATCHES(11, "not matches"),

    ;


    private int code;
    private String value;

    OperatorType(int code, String note) {
        this.code = code;
        this.value = note;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static OperatorType getEnum(int code) {
        OperatorType type =
                Arrays.stream(values()).filter(t -> t.getCode() == code).findAny().orElse(null);
        return type;
    }

}
