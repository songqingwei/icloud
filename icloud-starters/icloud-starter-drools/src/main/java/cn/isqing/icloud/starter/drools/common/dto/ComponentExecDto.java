package cn.isqing.icloud.starter.drools.common.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class ComponentExecDto {

    private Integer domain;

    private String domainAuthCode;

    // 变量服务组件id，json
    private Map<Long, String> variableAboveResMap;

    // 组件id，json
    private Map<Long, String> aboveResMap;

    private String inputParams;
    // 规则执行结果
    private String runRes;

}
