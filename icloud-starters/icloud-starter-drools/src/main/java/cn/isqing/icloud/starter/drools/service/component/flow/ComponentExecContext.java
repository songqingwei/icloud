package cn.isqing.icloud.starter.drools.service.component.flow;

import cn.isqing.icloud.common.utils.flow.FlowContext;
import cn.isqing.icloud.starter.drools.common.dto.ComponentExecDto;
import cn.isqing.icloud.starter.drools.dao.entity.Component;
import lombok.Data;

import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class ComponentExecContext extends FlowContext<Object> {
    private Component component;
    // 数据源配置项
    Map<String, Object> dsConfig;
    // 组件方言配置项
    private String dialectConfig;
    // 上文组件结果集
    private ComponentExecDto execDto;

    // 组件配置 start----
    private Map<String, String> dependInputParams;
    private Map<String, String> dependCRes;
    private Map<String, String> dependConstants;
    private Map<String, String> selfConstants;
    private Map<String, String> dependSystemVars;
    private Map<String, String> dependRunRes;
    private Map<String, Long> dependVariables;

    private String[] resJudge;
    // 组件配置 end----

    private Map<String, String> constantsValue;
    private Map<String, String> systemVarsValue;
    private Map<Long, Object> variablesValue;

    // 组件对外请求参数模版
    private String[] requestParamsTpl;

    private Object requestDto;

    private String execRes;
}
