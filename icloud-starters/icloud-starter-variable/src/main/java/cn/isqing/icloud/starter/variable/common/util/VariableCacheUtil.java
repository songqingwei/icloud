package cn.isqing.icloud.starter.variable.common.util;

import cn.isqing.icloud.starter.variable.common.dto.ActuatorDto;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
public class VariableCacheUtil {
    private VariableCacheUtil() {
    }

    public static final Map<Long, ActuatorDto> actuatorMap = new ConcurrentHashMap<>();

}
