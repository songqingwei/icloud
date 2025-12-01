package cn.isqing.icloud.common.utils.dto;

import cn.hutool.core.util.StrUtil;
import cn.isqing.icloud.common.utils.annotation.RouteType;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class BaseFactory<T> {

    private String temlate = "%s:%s:%s";

    @Autowired
    List<T> list;

    private Map<String, List<T>> map = new HashMap<>();

    public boolean isSupport(String... r){
        return map.containsKey(getKey(r));
    }

    public List<T> get(String... r){
        return map.get(getKey(r));
    }

    public String getKey(String... args){
        // 占位符个数
        int num = 3;
        if(args.length>=3){
            return String.format(temlate, args);
        }else{
            String[] newArgs = new String[num];
            // 将参数数组复制到新数组中
            System.arraycopy(args, 0, newArgs, 0, args.length);
            // 将新数组中缺少的元素用空字符串填充
            for (int i = args.length; i < num; i++) {
                newArgs[i] = "";
            }
            return String.format(temlate, newArgs);
        }
    }


    public T getSingle(String... r){
        return map.get(getKey(r)).get(0);
    }

    @PostConstruct
    public void init() {
        list.forEach(o -> {
            RouteType routeType = o.getClass().getAnnotation(RouteType.class);
            String key = getKey(routeType.r1(), routeType.r2(), routeType.r3());
            map.computeIfAbsent(key,k->new ArrayList<>()).add(o);
        });
    }
}
