package cn.isqing.icloud.starter.drools.service.output.flow;

import cn.isqing.icloud.common.utils.annotation.RouteType;
import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.common.utils.enums.ResCodeEnum;
import cn.isqing.icloud.common.utils.enums.status.CommonStatusEnum;
import cn.isqing.icloud.common.utils.enums.status.SubFlowStatusEnum;
import cn.isqing.icloud.common.utils.flow.FlowTemplate;
import cn.isqing.icloud.common.utils.kit.LockUtil;
import cn.isqing.icloud.common.utils.kit.ParallelStreamUtil;
import cn.isqing.icloud.common.utils.kit.RedisUtil;
import cn.isqing.icloud.starter.drools.common.constants.EventTypeConstants;
import cn.isqing.icloud.starter.drools.common.constants.LockScenarioConstants;
import cn.isqing.icloud.starter.drools.common.constants.RunLogTextTypeConstants;
import cn.isqing.icloud.starter.drools.common.dto.ComponentExecDto;
import cn.isqing.icloud.starter.drools.common.dto.RuleKeyDto;
import cn.isqing.icloud.starter.drools.common.util.KieUtil;
import cn.isqing.icloud.starter.drools.common.util.ObjectTransformUtil;
import cn.isqing.icloud.starter.drools.dao.entity.*;
import cn.isqing.icloud.starter.drools.dao.mapper.ActionLogMapper;
import cn.isqing.icloud.starter.drools.dao.mapper.RuleCoreMapper;
import cn.isqing.icloud.starter.drools.dao.mapper.RunLogMapper;
import cn.isqing.icloud.starter.drools.dao.mapper.RunLogTextMapper;
import cn.isqing.icloud.starter.drools.service.component.ComponentExecService;
import cn.isqing.icloud.starter.drools.service.component.factory.ComponentExecFactory;
import cn.isqing.icloud.starter.drools.service.event.EventSubscriber;
import cn.isqing.icloud.starter.drools.service.msg.dto.EventMsg;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
@Slf4j
@RouteType(r1 = EventTypeConstants.OUTPUT)
public class OutputFlow extends FlowTemplate<OutputFlowContext, Object> implements EventSubscriber {

    @Value("${i.drools.execActionTimeOut:60000}")
    private int execActionTimeOut;

    @Autowired
    private RunLogMapper mapper;
    @Autowired
    private RunLogTextMapper textMapper;
    @Autowired
    private RuleCoreMapper coreMapper;
    @Autowired
    private ComponentExecFactory execFactory;
    @Autowired
    private ActionLogMapper actionLogMapper;
    private int subFailReasonLimit = 255;

    public OutputFlow() {
        start("输出处理流程", this);
        stepName("获取log记录");
        accept(this::getRecord);
        stepName("获取core记录");
        accept(this::getCoreRecord);
        stepName("获取redis锁");
        accept(this::getLock);
        stepName("获取乐观锁锁");
        accept(this::getCasLock);
        stepName("记录处理中状态");
        accept(this::recordDoingStatus);
        stepName("获取变量值");
        accept(this::getVarValue);
        stepName("执行action");
        accept(this::doAction);
        stepName("更新log状态");
        accept(this::updateStatus);
        finallyAcceptName("释放资源");
        finallyAccept(this::releaseResource);
    }

    private void getVarValue(OutputFlowContext context) {
        RunLog runLog = context.getRunLog();
        RunLogText condition = new RunLogText();
        condition.setFid(runLog.getId());
        List<RunLogText> texts = textMapper.selectByCondition(condition);
        Map<Integer, String> map = texts.stream().collect(Collectors.groupingBy(RunLogText::getType,
                Collectors.mapping(RunLogText::getText, Collectors.joining())));
        context.setParamsMap(map);
    }

    private void updateStatus(OutputFlowContext context) {
        RunLog runLog = context.getRunLog();
        RunLog data = context.getCacheLog();
        data.setId(runLog.getId());
        data.setSubStatus(SubFlowStatusEnum.SUCCESS.getCode());
        mapper.update(data);

        data.setSubStatus(null);
    }

    private void releaseResource(OutputFlowContext context) {
        RLock lock = context.getLock();
        if(lock!=null){
            lock.unlock();
        }
        if(context.isCasLock()){
            LockUtil.unlockPo(context,context.getRunLog(),mapper);
        }
    }

    private void doAction(OutputFlowContext context) {
        Map<Integer, String> map = context.getParamsMap();
        ComponentExecDto paramDto = new ComponentExecDto();
        paramDto.setDomain(context.getRuleCore().getDomain());
        //todo-sqw
        paramDto.setDomainAuthCode("");
        paramDto.getVariableAboveResMap().putAll(JSONObject.parseObject(map.get(RunLogTextTypeConstants.VC_RES_MAP),new TypeReference<Map<String,Object>>(){}));
        paramDto.setInputParams(JSONObject.parseObject(map.get(RunLogTextTypeConstants.INPUT_PARAMS),new TypeReference<Map<String,Object>>(){}));
        paramDto.setRunRes(map.get(RunLogTextTypeConstants.INPUT_PARAMS));

        RuleCore ruleCore = context.getRuleCore();
        RuleKeyDto ruleKeyDto = ObjectTransformUtil.transform(ruleCore, RuleKeyDto.class);

        RunLog runLog = context.getRunLog();
        ActionLog actionLog = new ActionLog();
        actionLog.setRunLogId(runLog.getId());

        List<List<Component>> componentList = KieUtil.actionMap.get(ruleKeyDto);
        Consumer<Component> consumer = (c) -> {
            actionLog.setCid(c.getId());
            ActionLog first = actionLogMapper.first(actionLog, null);
            if(first!=null && first.getStatus()>CommonStatusEnum.FAILED.getCode()){
                return;
            }
            ComponentExecService service = execFactory.getSingle(c.getDataSourceType().toString());
            Response<Object> res = service.exec(c, paramDto);
            if (!res.isSuccess()) {
                log.error(res.getMsg());
                // 更新数据库失败次数及原因
                updataLog(context.getCacheLog(), runLog, res);
                interrupt(context, Response.error("执行异常"));
            }
            updataActionLog(first,runLog, res,c);
        };
        componentList.forEach(list -> {
            if(context.isFlowEnd()){
                return;
            }
            try {
                ParallelStreamUtil.exec(list, consumer, execActionTimeOut);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });



    }

    private void updataActionLog(ActionLog first, RunLog runLog, Response<Object> res,Component c) {
        if(first==null){
            first = new ActionLog();
            first.setCoreId(runLog.getCoreId());
            first.setRunLogId(runLog.getId());
            first.setCid(c.getId());
            first.setFailNum(0);
        }
        if(res.isSuccess()){
            first.setStatus(CommonStatusEnum.SUCCESS.getCode());
        } else {
            first.setStatus(CommonStatusEnum.FAILED.getCode());
            first.setFailNum(first.getFailNum()+1);
            first.setFailReason(res.getMsg());
        }
        if(first.getId()==null){
            actionLogMapper.insert(first);
            return;
        }
        actionLogMapper.update(first);
    }

    private void updataLog(RunLog cacheLog, RunLog runLog, Response<Object> res) {
        cacheLog.setId(runLog.getId());
        cacheLog.setSubFailNum(runLog.getSubFailNum()+1);
        cacheLog.setSubStatus(SubFlowStatusEnum.FAILED.getCode());
        cacheLog.setSubFailReason(res.getMsg().substring(0,subFailReasonLimit));
        mapper.update(cacheLog);

        cacheLog.setSubFailNum(null);
        cacheLog.setSubStatus(null);
        cacheLog.setSubFailReason(null);
    }

    private void getCoreRecord(OutputFlowContext context) {
        RunLog runLog = context.getRunLog();
        RuleCore ruleCore = coreMapper.selectById(runLog.getId(),RuleCore.class);
        context.setRuleCore(ruleCore);
    }

    private void recordDoingStatus(OutputFlowContext context) {
        RunLog runLog = context.getRunLog();
        RunLog data = context.getCacheLog();
        data.setId(runLog.getId());
        data.setSubStatus(SubFlowStatusEnum.DOING.getCode());
        data.setVersion(runLog.getVersion() + 1);
        RunLog condition = new RunLog();
        condition.setVersion(runLog.getVersion());
        int i = mapper.updateByCondition(data, condition);
        if (i == 0) {
            interrupt(context, Response.error("更新处理中状态失败"));
        }else {
            runLog.setVersion(data.getVersion());
        }
        //清理cacheLog
        data.setVersion(null);
        data.setSubStatus(null);
    }

    private void getCasLock(OutputFlowContext context) {
        boolean b;
        try {
            b = LockUtil.lockPo(context, context.getRunLog(), mapper);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            b = false;
        }
        context.setCasLock(b);
        if (!b) {
            interrupt(context, Response.error("获取乐观锁失败"));
        }
    }

    private void getLock(OutputFlowContext context) {
        RLock lock = LockUtil.getRedisLock(RedisUtil.getKey(LockScenarioConstants.OUTPUT,
                context.getMsg().getId().toString()));
        if (lock == null) {
            interrupt(context, Response.error("竞争redis锁失败"));
            return;
        }
        context.setLock(lock);
    }

    private void getRecord(OutputFlowContext context) {
        RunLog runLog = mapper.selectById(context.getMsg().getId(), RunLog.class);
        if (runLog == null) {
            interrupt(context, Response.info(ResCodeEnum.NOTFIND.getCode(), ResCodeEnum.NOTFIND.getMsg()));
            return;
        }
        context.setRunLog(runLog);
        // 状态判断
        if (runLog.getStatus() >= SubFlowStatusEnum.SUCCESS.ordinal()) {
            interrupt(context, Response.error("状态完结，无需处理"));
        }
    }


    @Override
    public void onEvent(EventMsg eventMsg) {
        OutputFlowContext context = new OutputFlowContext();
        context.setMsg(eventMsg);
        exec(context);
    }
}
