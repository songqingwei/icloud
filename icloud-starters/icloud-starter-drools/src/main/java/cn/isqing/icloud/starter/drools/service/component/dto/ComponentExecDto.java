package cn.isqing.icloud.starter.drools.service.component.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class ComponentExecDto {

    // 变量组件id，json
    private Map<Long, String> variableAboveResMap;
    // 组件id，json
    private Map<Long, String> aboveResMap;

    private String inputParams;
    // 规则执行结果
    private String runRes;

}
