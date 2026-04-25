package cn.isqing.icloud.common.utils.bean;

import cn.isqing.icloud.common.api.enums.ResCodeEnum;
import cn.isqing.icloud.common.utils.annotation.RouteType;
import cn.isqing.icloud.common.utils.dto.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
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
@Slf4j
public class BaseFactory<T> {
    private String temlate = "%s:%s:%s";

    @Autowired(required = false)
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
        List<T> result = map.get(getKey(r));
        if (result == null || result.isEmpty()) {
            throw new BaseException(ResCodeEnum.SYSTEM_ERROR.getCode(),
                    "未找到对应的实现类: " + getKey(r));
        }
        return result.get(0);
    }

    @PostConstruct
    public void init() {
        if (list == null || list.isEmpty()) {
            log.info("BaseFactory: 未找到任何 {} 类型的实现类，工厂已初始化为空", 
                    getClass().getGenericSuperclass().getTypeName());
            return;
        }
        
        list.forEach(o -> {
            Class<?> targetClass = AopUtils.getTargetClass(o);
            RouteType routeType = targetClass.getAnnotation(RouteType.class);
            if (routeType == null) {
                log.warn("BaseFactory: {} 未标注 @RouteType，已跳过路由注册", targetClass.getName());
                return;
            }
            String key = getKey(routeType.r1(), routeType.r2(), routeType.r3());
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(o);
        });
        
        log.info("BaseFactory: 成功注册 {} 个路由规则", map.size());
    }
}
