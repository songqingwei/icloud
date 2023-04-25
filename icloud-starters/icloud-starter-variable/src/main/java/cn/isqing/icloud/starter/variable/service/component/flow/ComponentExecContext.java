package cn.isqing.icloud.starter.variable.service.component.flow;

import cn.isqing.icloud.common.utils.flow.FlowContext;
import cn.isqing.icloud.starter.variable.common.dto.ComponentExecDto;
import cn.isqing.icloud.starter.variable.dao.entity.Component;
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
    // 上文结果集
    private ComponentExecDto resDto;

    // 组件配置 start----
    private Map<String, String> aboveResParams;
    private Map<String, String> inputParams;

    private Map<String, String> constantParams;
    private Map<String, String> variableParams;
    private String resJudge;
    // 组件配置 end----

    private Map<String, String> constantMap;
    private Map<String, String> variableMap;

    private String[] requestParams;
    private Object requestDto;

    private String execRes;
}
