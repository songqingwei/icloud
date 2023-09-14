package cn.isqing.icloud.starter.variable.common.enums;

import java.util.Arrays;

public enum FunctionComponentDialectType {
    METHOD_NAME(1, "$.method_name", "method_name"),
    PARAMS(2, "$.params", "params"),
    ;


    private int code;
    private String jsonPath;
    private String note;

    FunctionComponentDialectType(int code, String jsonPath, String note) {
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

    public static FunctionComponentDialectType getEnum(int code) {
        FunctionComponentDialectType type =
                Arrays.stream(values()).filter(t -> t.getCode() == code).findAny().orElse(null);
        return type;
    }

}
