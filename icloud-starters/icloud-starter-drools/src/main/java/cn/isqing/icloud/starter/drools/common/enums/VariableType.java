package cn.isqing.icloud.starter.drools.common.enums;

import java.util.Arrays;

public enum VariableType {

    STRING("java.lang.String", "String"),
    BIG_DECIMAL("java.math.BigDecimal", "BigDecimal"),
    BIG_INTEGER("java.math.BigInteger", "BigInteger"),
    ;

    private String name;
    private String simpleName;

    VariableType(String name, String simpleName) {
        this.name = name;
        this.simpleName = simpleName;
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
        return type==null?null:type.getSimpleName();
    }
    public static String getSimpleName(String name) {
        VariableType type =
                Arrays.stream(values()).filter(t -> t.getName().equals(name)).findAny().orElse(null);
        return type==null?null:type.getName();
    }

}
