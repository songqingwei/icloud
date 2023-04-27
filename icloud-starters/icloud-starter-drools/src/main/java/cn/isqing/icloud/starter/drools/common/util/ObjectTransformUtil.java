package cn.isqing.icloud.starter.drools.common.util;

import cn.isqing.icloud.starter.drools.common.dto.RuleKeyDto;
import cn.isqing.icloud.starter.drools.dao.entity.RuleCore;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 对象转换工具
 * 如果转换方法很多，可以将方法定义在别的文件，这里import后 注册即可
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public class ObjectTransformUtil {

    public static final Map<Class, Map<Class, Function<Object, Object>>> MAP = new HashMap<>();

    public static <T> T transform(Object o, Class<T> tClass) {
        return (T) MAP.get(o.getClass()).get(tClass).apply(o);
    }

    public static <L, R> void register(Class<L> l, Class<R> r, Function<L, R> function) {
        Map<Class, Function<Object, Object>> map = MAP.computeIfAbsent(l, l1 -> new HashMap<>());
        map.put(r, (Function<Object, Object>) function);
    }

    public static Function<RuleCore, RuleKeyDto> function1 = ruleCore -> {
        RuleKeyDto keyDto = new RuleKeyDto();
        keyDto.setDomain(ruleCore.getDomain());
        keyDto.setBusiCode(ruleCore.getBusiCode());
        keyDto.setActionId(ruleCore.getActionId());
        return keyDto;
    };

    // 注册方法定义
    static {
        register(RuleCore.class, RuleKeyDto.class, function1);
    }

}
