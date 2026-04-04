package cn.isqing.icloud.common.utils.json;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeWriter implements ObjectWriter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void write(JSONWriter jsonWriter, Object object, Object fieldName, Type fieldType, long features) {
        if (object instanceof LocalDateTime) {
            LocalDateTime dateTime = (LocalDateTime) object;
            jsonWriter.writeString(formatter.format(dateTime));
        }
    }
}