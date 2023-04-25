package cn.isqing.icloud.common.utils.dto;

import cn.hutool.core.util.StrUtil;
import cn.isqing.icloud.common.utils.annotation.RouteType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class BaseFactory<T> {

    private String temlate = "{}:{}:{}";

    @Autowired
    List<T> list;

    private Map<String, List<T>> map = new HashMap<>();

    public boolean isSupport(String... r){
        return map.containsKey(StrUtil.format(temlate, r));
    }

    public List<T> get(String... r){
        return map.get(StrUtil.format(temlate, r));
    }

    public T getSingle(String... r){
        return map.get(StrUtil.format(temlate, r)).get(0);
    }

    @PostConstruct
    public void init() {
        list.forEach(o -> {
            RouteType routeType = o.getClass().getAnnotation(RouteType.class);
            String key = StrUtil.format(temlate, routeType.r1(), routeType.r2(), routeType.r3());
            map.computeIfAbsent(key,k->new ArrayList<>()).add(o);
        });
    }
}
