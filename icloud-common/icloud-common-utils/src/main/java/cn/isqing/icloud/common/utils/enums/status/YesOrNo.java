package cn.isqing.icloud.common.utils.enums.status;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum YesOrNo {
    NO("no"),
    YES("yes");
    private String note;

    YesOrNo(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    @JsonValue
    public int toValue() {
        return this.ordinal();
    }

    @JsonCreator
    public static YesOrNo fromValue(int value) {
        return values()[value];

    }
}
