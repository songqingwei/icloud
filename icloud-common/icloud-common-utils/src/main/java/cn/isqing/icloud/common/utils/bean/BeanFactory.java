package cn.isqing.icloud.common.utils.bean;


import java.util.HashMap;
import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class BeanFactory {

    //自己注册
    private static final Map<String, Object> contextMap = new HashMap<>();

    public static Object getContext(String name) {
        return contextMap.get(name);
    }

    public static void setContext(String name, Object obj) {
        contextMap.put(name, obj);
    }


}
