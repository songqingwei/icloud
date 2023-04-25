package cn.isqing.icloud.starter.variable.common.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class ComponentExecDto {

    // 组件id，json
    private Map<Long, String> aboveResMap;

    private String inputParams;

}
