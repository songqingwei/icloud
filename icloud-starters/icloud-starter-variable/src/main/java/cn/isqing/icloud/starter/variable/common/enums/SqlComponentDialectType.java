package cn.isqing.icloud.starter.variable.common.enums;

import java.util.Arrays;

public enum SqlComponentDialectType {

    SQL(1, "$.sql", "sql"),
    ;


    private int code;
    private String jsonPath;
    private String note;

    SqlComponentDialectType(int code, String jsonPath, String note) {
        this.code = code;
        this.jsonPath = jsonPath;
        this.note = note;
    }

    public int getCode() {
        return code;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    public String getNote() {
        return note;
    }

    public static SqlComponentDialectType getEnum(int code) {
        SqlComponentDialectType type =
                Arrays.stream(values()).filter(t -> t.getCode() == code).findAny().orElse(null);
        return type;
    }

}
