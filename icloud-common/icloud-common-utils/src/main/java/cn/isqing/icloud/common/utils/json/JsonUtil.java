package cn.isqing.icloud.common.utils.json;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import cn.isqing.icloud.common.api.dto.Response;
import com.alibaba.fastjson2.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author
 * @version 1.0
 **/
@Slf4j
public class JsonUtil {

    private static int capacity = 300;

    private static final LRUCache<String, JSONPath> CACHE = CacheUtil.newLRUCache(capacity);

    static {
        JSON.register(LocalDateTime.class, new LocalDateTimeWriter());
    }

    public static String toJsonString(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            log.warn("对象转json异常:{},{}", e.getMessage(), object);
        }
        return "";
    }

    public static Response<String> toJsonRes(Object object) {
        try {
            return Response.success(JSON.toJSONString(object));
        } catch (Exception e) {
            log.warn("对象转json异常:{},{}", e.getMessage(), object);
            return Response.error("对象转json异常");
        }
    }

    public static String toJsonStringWithNull(Object object) {
        try {
            return JSON.toJSONString(object, JSONWriter.Feature.WriteNulls);
        } catch (Exception e) {
            log.warn("对象转json异常:{},{}", e.getMessage(), object);
        }
        return "";
    }

    public static Object extract(String json, String path) {
        JSONPath jsonPath = CACHE.get(path);
        if (jsonPath == null) {
            jsonPath = JSONPath.of(path);
            CACHE.put(path, jsonPath);
        }
        JSONReader jsonReader = JSONReader.of(json);
        return jsonPath.extract(jsonReader);
    }

    public static boolean extractAndEqualWith(String json, String path, String target) {
        Object val = extract(json, path);
        return target.equals(val);
    }

    public static String extractString(String json, String path) {
        Object val = extract(json, path);
        if (val == null) {
            return null;
        }
        return val.toString();
    }

    public static <T> T extractJavaObj(String json, String path,Class<T> clazz) {
        Object object = extract(json, path);
        if (object == null) {
            return null;
        }
        // 如果输入对象已经是目标类型，则直接返回
        if (clazz.isInstance(object)) {
            return clazz.cast(object);
        }
        // 使用 Fastjson2 进行对象转换
        return JSON.parseObject(JSON.toJSONString(object), clazz);
    }

    public static <R> List<R> toList(List<JSONObject> list, Class<R> c) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        return list.stream().map(o -> o.to(c)).collect(Collectors.toList());
    }

}
