package cn.isqing.icloud.starter.drools.service.output.flow;

import cn.isqing.icloud.common.utils.flow.FlowContext;
import cn.isqing.icloud.starter.drools.dao.entity.RuleCore;
import cn.isqing.icloud.starter.drools.dao.entity.RunLog;
import cn.isqing.icloud.starter.drools.service.msg.dto.EventMsg;
import lombok.Data;
import org.redisson.api.RLock;

import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class OutputFlowContext extends FlowContext {

    private EventMsg msg;

    private RLock lock;
    private boolean casLock = false;

    private RunLog runLog;
    private Map<Integer, String> paramsMap;
    private RunLog cacheLog = new RunLog();

    private RuleCore ruleCore;

}
