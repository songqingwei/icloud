package cn.isqing.icloud.common.utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * GSON工具
 * 注意：gson解决不了循环引用的问题，请不要用在循环引用的对象中，否则内存溢出
 *
 * @author songqingwei
 * @version 1.0
 **/
@Slf4j
public class GsonUtil {
    private static final Gson GSON;
    //不过滤空值
    private static final Gson GSON_WITH_NULL;

    static {
        GSON = new GsonBuilder().enableComplexMapKeySerialization()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .disableHtmlEscaping()//不转义html字符
                .create();
        GSON_WITH_NULL = new GsonBuilder().enableComplexMapKeySerialization()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .disableHtmlEscaping()
                .serializeNulls()//当字段值为null时，依然对改字段进行处理
                .create();
    }

    public static String toJsonString(Object object) {
        try {
            return GSON.toJson(object);
        } catch (Exception e) {
            log.warn("对象转json异常:{},{}", e.getMessage(), object);
        }
        return "";
    }

}
