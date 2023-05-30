package cn.isqing.icloud.common.utils.json;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.JSONReader;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author songqingwei
 * @version 1.0
 **/
@Slf4j
public class JsonUtil {

    private static int capacity = 300;

    private static final LRUCache<String, JSONPath> CACHE = CacheUtil.newLRUCache(capacity);

    public static String toJsonString(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            log.warn("对象转json异常:{},{}", e.getMessage(), object);
        }
        return "";
    }

    public static Object extract(String json, String path){
        JSONPath jsonPath = CACHE.get(path);
        if(jsonPath==null){
            jsonPath = JSONPath.of(path);
            CACHE.put(path,jsonPath);
        }
        JSONReader jsonReader = JSONReader.of(json);
        return jsonPath.extract(jsonReader);
    }

    public static <R> List<R> toList(List<JSONObject> list,Class c){
        if(list==null || list.isEmpty()){
            return Collections.emptyList();
        }
        return (List<R>) list.stream().map(o->o.to(c)).collect(Collectors.toList());
    }

}
