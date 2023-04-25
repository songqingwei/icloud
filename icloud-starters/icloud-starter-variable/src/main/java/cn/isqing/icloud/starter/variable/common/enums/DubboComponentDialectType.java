package cn.isqing.icloud.starter.variable.common.enums;

import java.util.Arrays;

public enum DubboComponentDialectType {

    METHOD_TYPE(1, "$.1", "method_type"),
    INTERFACENAME(2, "$.2", "interfacename"),
    METHOD_NAME(3, "$.3", "method_name"),
    VERSION(4, "$.4", "version"),
    GROUP(5, "$.5", "group"),
    PARAMS(6, "$.6", "params"),
    ;


    private int code;
    private String jsonPath;
    private String note;

    DubboComponentDialectType(int code, String jsonPath, String note) {
        this.code = code;
        this.jsonPath = jsonPath;
        this.note = note;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public int getCode() {
        return code;
    }

    public String getNote() {
        return note;
    }

    public static DubboComponentDialectType getEnum(int code) {
        DubboComponentDialectType type =
                Arrays.stream(values()).filter(t -> t.getCode() == code).findAny().orElse(null);
        return type;
    }

}
