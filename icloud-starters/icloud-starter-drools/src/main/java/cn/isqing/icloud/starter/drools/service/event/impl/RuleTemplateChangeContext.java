package cn.isqing.icloud.starter.drools.service.event.impl;

import cn.isqing.icloud.common.utils.flow.FlowContext;
import cn.isqing.icloud.starter.drools.common.dto.RuleKeyDto;
import cn.isqing.icloud.starter.drools.dao.entity.Component;
import cn.isqing.icloud.starter.drools.dao.entity.RuleCore;
import cn.isqing.icloud.starter.drools.service.msg.dto.TplChangeMsg;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariableDto;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariableSimpleDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class RuleTemplateChangeContext extends FlowContext {
    private String msgReq;
    private TplChangeMsg msg;
    private RuleKeyDto ruleKeyDto;

    // 所需要的所有组件map
    Map<Long, Component> allComponent;

    private Map<String, ApiVariableDto> variableMap;
    private List<Long> actionDepandCids;

    private RuleCore core;

    private boolean lock = false;
    private LocalDateTime dealTime;

}
