package cn.isqing.icloud.starter.drools.service.input.flow;

import cn.isqing.icloud.common.utils.enums.status.SubFlowStatusEnum;
import cn.isqing.icloud.common.utils.flow.FlowContext;
import cn.isqing.icloud.starter.drools.common.dto.RuleKeyDto;
import cn.isqing.icloud.starter.drools.dao.entity.RunLog;
import cn.isqing.icloud.starter.drools.service.input.dto.InputDto;
import lombok.Data;
import org.redisson.api.RLock;

import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class InputFlowContext extends FlowContext<Object> {

    private InputDto inputDto;

    private RuleKeyDto ruleKeyDto;

    private Long coreId;

    // 组件结果集
    private Map<Long,String> resMap;


    private RLock lock;

    private RunLog runLog;

    private Long ruleId;
    private Long targetId;

    private boolean cancleSubFlow = false;


}
