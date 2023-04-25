package cn.isqing.icloud.starter.drools.common.enums;

import java.util.Arrays;

/**
 * 分配模式
 */
public enum AllocationModel {

    FIXED_NUM(1, "固定数量"),
    SINGLE_RATIO(2, "比例"),
    RATIO(3, "高精度比例"),
    ;


    private int code;
    private String note;

    AllocationModel(int code, String note) {
        this.code = code;
        this.note = note;
    }

    public int getCode() {
        return code;
    }

    public String getNote() {
        return note;
    }

    public static AllocationModel getEnum(int code) {
        AllocationModel type =
                Arrays.stream(values()).filter(t -> t.getCode() == code).findAny().orElse(null);
        return type;
    }

}
