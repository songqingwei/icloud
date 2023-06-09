package cn.isqing.icloud.starter.variable.api.enums;

import java.util.Arrays;

public enum VariableType {

    STRING(1, "java.lang.String", "String"),
    BIG_DECIMAL(2, "java.math.BigDecimal", "BigDecimal"),
    BIG_INTEGER(3, "java.math.BigInteger", "BigInteger"),
    LIST(4, "java.util.List", "List"),
    MAP(5, "java.util.Map", "Map"),
    LONG(6, "java.lang.Long", "Long"),
    ;

    private Integer code;
    private String name;
    private String simpleName;

    VariableType(Integer code, String name, String simpleName) {
        this.code = code;
        this.name = name;
        this.simpleName = simpleName;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public static String getName(String simpleName) {
        VariableType type =
                Arrays.stream(values()).filter(t -> t.getSimpleName().equals(simpleName)).findAny().orElse(null);
        return type == null ? null : type.getSimpleName();
    }

    public static String getSimpleName(String name) {
        VariableType type =
                Arrays.stream(values()).filter(t -> t.getName().equals(name)).findAny().orElse(null);
        return type == null ? null : type.getName();
    }

    public static VariableType fromCode(Integer code) {
        return Arrays.stream(values()).filter(t -> t.getCode().equals(code)).findAny().orElse(null);
    }
}
