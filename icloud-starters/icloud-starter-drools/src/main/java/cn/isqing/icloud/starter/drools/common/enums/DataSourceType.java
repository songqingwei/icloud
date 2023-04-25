package cn.isqing.icloud.starter.drools.common.enums;

import java.util.Arrays;

public enum DataSourceType {

    DUBBO(1, "dubbo"),
    SQL(2, "sql"),
    ;


    private int code;
    private String note;

    DataSourceType(int code, String note) {
        this.code = code;
        this.note = note;
    }

    public int getCode() {
        return code;
    }

    public String getNote() {
        return note;
    }

    public static DataSourceType getEnum(int code) {
        DataSourceType type =
                Arrays.stream(values()).filter(t -> t.getCode() == code).findAny().orElse(null);
        return type;
    }

}
