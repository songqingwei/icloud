package cn.isqing.icloud.starter.variable.common.dto;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class ComponentExecDto {

    // 组件id，json
    private Map<Long, String> aboveResMap = new ConcurrentHashMap<>();

    private String inputParams;

}
