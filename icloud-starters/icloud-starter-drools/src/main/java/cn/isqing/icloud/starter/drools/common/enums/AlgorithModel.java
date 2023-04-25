package cn.isqing.icloud.starter.drools.common.enums;

import java.util.Arrays;

/**
 * 算法模式
 */
public enum AlgorithModel {

    S_SHAPED(1, "蛇形算法"),
    RANDOM(2, "随机算法"),
    ;


    private int code;
    private String note;

    AlgorithModel(int code, String note) {
        this.code = code;
        this.note = note;
    }

    public int getCode() {
        return code;
    }

    public String getNote() {
        return note;
    }

    public static AlgorithModel getEnum(int code) {
        AlgorithModel type =
                Arrays.stream(values()).filter(t -> t.getCode() == code).findAny().orElse(null);
        return type;
    }

}
