package cn.isqing.icloud.starter.drools.service.event.impl;

import cn.isqing.icloud.common.utils.flow.FlowContext;
import cn.isqing.icloud.starter.drools.common.dto.RuleKeyDto;
import cn.isqing.icloud.starter.drools.common.dto.SyncResVariableDto;
import cn.isqing.icloud.starter.drools.dao.entity.RuleCore;
import cn.isqing.icloud.starter.drools.service.msg.dto.TplChangeMsg;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariableDto;
import lombok.Data;

import java.time.LocalDateTime;
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

    private Map<String, ApiVariableDto> variableMap;
    private Map<Long, SyncResVariableDto> syncResMap;
    private List<Long> actionDepandCids;
    // 执行规则所需的所有变量id
    private List<Long> vidList;

    private RuleCore core;

    private boolean lock = false;
    private LocalDateTime dealTime;

}
