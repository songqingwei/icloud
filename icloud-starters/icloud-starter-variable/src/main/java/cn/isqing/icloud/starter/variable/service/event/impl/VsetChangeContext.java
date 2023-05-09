package cn.isqing.icloud.starter.variable.service.event.impl;

import cn.isqing.icloud.common.utils.flow.FlowContext;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariableSimpleDto;
import cn.isqing.icloud.starter.variable.service.msg.dto.EventMsg;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class VsetChangeContext extends FlowContext {
    // 入参
    private EventMsg eventMsg;

    // 可选入参：空 则需要从EventMsg 自己解析
    private List<Long> vidList;

    private Map<Long, ApiVariableSimpleDto> variableMap;


}
