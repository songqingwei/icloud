package cn.isqing.icloud.starter.drools.service.event.impl;

import cn.isqing.icloud.common.utils.flow.FlowContext;
import cn.isqing.icloud.starter.drools.common.dto.RuleKeyDto;
import cn.isqing.icloud.starter.drools.dao.entity.Component;
import cn.isqing.icloud.starter.drools.dao.entity.RuleCore;
import cn.isqing.icloud.starter.drools.service.msg.dto.TplChangeMsg;
import cn.isqing.icloud.starter.variable.api.dto.VariableSimpleDto;
import lombok.Data;

import java.util.List;
import java.util.Map;

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

    private Map<String,VariableSimpleDto> variableMap;
    private List<Long> actionDepandCids;

    private RuleCore core;

}
