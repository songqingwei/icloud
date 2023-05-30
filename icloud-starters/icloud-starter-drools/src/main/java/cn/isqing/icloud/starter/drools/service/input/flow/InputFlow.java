package cn.isqing.icloud.starter.drools.service.input.flow;

import cn.isqing.icloud.common.utils.constants.SqlConstants;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.enums.status.CommonStatusEnum;
import cn.isqing.icloud.common.utils.enums.status.SubFlowStatusEnum;
import cn.isqing.icloud.common.utils.flow.FlowTemplate;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import cn.isqing.icloud.common.utils.kit.LockUtil;
import cn.isqing.icloud.common.utils.kit.RedisUtil;
import cn.isqing.icloud.common.utils.time.TimeUtil;
import cn.isqing.icloud.common.utils.validation.ValidationUtil;
import cn.isqing.icloud.starter.drools.common.constants.*;
import cn.isqing.icloud.starter.drools.common.dto.RuleKeyDto;
import cn.isqing.icloud.starter.drools.common.util.KieUtil;
import cn.isqing.icloud.starter.drools.common.util.TextSqlUtil;
import cn.isqing.icloud.starter.drools.dao.entity.RuleCoreCondition;
import cn.isqing.icloud.starter.drools.dao.entity.RunLog;
import cn.isqing.icloud.starter.drools.dao.entity.RunLogText;
import cn.isqing.icloud.starter.drools.dao.mapper.RuleCoreMapper;
import cn.isqing.icloud.starter.drools.dao.mapper.RunLogMapper;
import cn.isqing.icloud.starter.drools.dao.mapper.RunLogTextMapper;
import cn.isqing.icloud.starter.drools.service.component.factory.ComponentExecFactory;
import cn.isqing.icloud.starter.drools.service.event.EventPublisher;
import cn.isqing.icloud.starter.drools.service.input.dto.InputDto;
import cn.isqing.icloud.starter.variable.api.VariableInterface;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariablesValueReqDto;
import cn.isqing.icloud.starter.variable.api.util.VariableUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.api.definition.type.FactType;
import org.kie.api.runtime.StatelessKieSession;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
@Slf4j
public class InputFlow extends FlowTemplate<InputFlowContext, Object> {

    @Autowired
    private RuleCoreMapper coreMapper;
    @Autowired
    private RunLogMapper logMapper;
    @Autowired
    private ComponentExecFactory execFactory;

    @Autowired
    private VariableInterface variableInterface;

    @Autowired
    private RunLogTextMapper textMapper;
    @Autowired
    private EventPublisher eventPublisher;

    public InputFlow() {
        start("输入处理流程", this);
        stepName("参数校验");
        accept(this::checkParam);
        stepName("获取coreId");
        accept(this::getCoreId);
        stepName("获取redis锁");
        accept(this::getLock);
        stepName("初始化log记录");
        accept(this::initLog);
        stepName("获取乐观锁");
        accept(this::getCasLock);
        stepName("获取RuleKeyDto");
        accept(this::getRuleKeyDto);
        stepName("获取变量");
        accept(this::getVariables);
        stepName("执行规则");
        accept(this::runRule);
        stepName("更新log记录");
        accept(this::updateLog);
        stepName("触发action事件");
        test(c->!c.isCancleSubFlow());
        accept(this::publishEvent);
        finallyAcceptName("释放资源");
        finallyAccept(this::releaseResource);
    }

    private void checkParam(InputFlowContext context) {
        InputDto inputDto = context.getInputDto();
        List<String> list = ValidationUtil.validate(inputDto);
        if (!list.isEmpty()) {
            interrupt(context, Response.error(JsonUtil.toJsonString(list)));
        }
    }

    private void releaseResource(InputFlowContext context) {
        RLock lock = context.getLock();
        if (lock != null) {
            lock.unlock();
            context.setLock(null);
        }
        if (context.isCasLock()) {
            LockUtil.unlockPo(context, context.getRunLog(), logMapper);
        }
    }

    private void publishEvent(InputFlowContext context) {
        eventPublisher.publishEvent(context.getRunLog().getId(), EventTypeConstants.OUTPUT);
    }

    private void updateLog(InputFlowContext context) {
        RunLog runLog = context.getRunLog();

        RunLog log = new RunLog();
        log.setId(runLog.getId());
        log.setTid(context.getRuleId());
        log.setTargetId(context.getTargetId());
        log.setStatus(CommonStatusEnum.SUCCESS.getCode());
        if (context.getTargetId() == null || context.getTargetId().equals(0L)) {
            log.setSubStatus(SubFlowStatusEnum.CANCLE.getCode());
            context.setCancleSubFlow(true);
        } else {
            log.setSubStatus(SubFlowStatusEnum.PENDING.getCode());
        }
        logMapper.update(log);

        RunLogText text = new RunLogText();
        text.setFid(runLog.getId());
        textMapper.delByCondition(text);

        Map<String, Object> map = new HashMap<>();
        map.put(FactDataConstants.TARGET_ID,log.getTargetId());
        map.put(FactDataConstants.RULE_ID,log.getTid());

        Object[][] arr = {
                {RunLogTextTypeConstants.VC_RES_MAP, context.getResMap()},
                {RunLogTextTypeConstants.INPUT_PARAMS, context.getInputDto().getParams()},
                {RunLogTextTypeConstants.RUN_RES_MAP, map}
        };
        for (Object[] arr1 : arr) {
            insetText(text, arr1[1], (int) arr1[0]);
        }
    }

    private void insetText(RunLogText text, Object o, int type) {
        TextSqlUtil.insertText(textMapper, text, o, t -> ((RunLogText) t).setType(type), s -> {
            text.setId(null);
            text.setText(s);
        });
    }

    @SneakyThrows
    private void runRule(InputFlowContext context) {
        KieBase kieBase = KieUtil.baseMap.get(context.getRuleKeyDto());
        StatelessKieSession session = kieBase.newStatelessKieSession();
        FactType factType = kieBase.getFactType(KieUtil.PACKAGE_NAME, "Data");
        Object data = factType.newInstance();
        factType.set(data, FactDataConstants.BUSI_DATE, TimeUtil.now().toLocalDate());
        factType.set(data, FactDataConstants.CORE_ID, context.getCoreId());
        Map<Long, String> resMap = context.getResMap();
        KieUtil.variableMap.get(context.getRuleKeyDto()).forEach((k, v) -> {
            Object value = VariableUtil.getValue(v,resMap);
            factType.set(data, k, value);
        });
        session.execute(data);

        Long ruleId = (Long) factType.get(data, FactDataConstants.RULE_ID);
        Long targetId = (Long) factType.get(data, FactDataConstants.TARGET_ID);
        context.setRuleId(ruleId);
        context.setTargetId(targetId);
    }

    private void getVariables(InputFlowContext context) {
        ApiVariablesValueReqDto reqDto = new ApiVariablesValueReqDto();
        reqDto.setCoreId(context.getCoreId().toString());
        reqDto.setInputParams(context.getInputDto().getParams());
        reqDto.setDomain(context.getInputDto().getDomain());
        reqDto.setDomainAuthCode(context.getInputDto().getDomainAuthCode());
        Response<Map<Long, String>> res = variableInterface.getComponentRes(reqDto);
        if(!res.isSuccess()){
            interrupt(context,Response.error(res.getMsg()));
            return;
        }
        context.setResMap(res.getData());
    }

    private void getRuleKeyDto(InputFlowContext context) {
        InputDto inputDto = context.getInputDto();
        RuleKeyDto ruleKeyDto = new RuleKeyDto();
        ruleKeyDto.setDomain(inputDto.getDomain());
        ruleKeyDto.setBusiCode(inputDto.getBusiCode());
        ruleKeyDto.setActionId(inputDto.getActionId());
        context.setRuleKeyDto(ruleKeyDto);
    }

    private void getCasLock(InputFlowContext context) {
        boolean b;
        try {
            b = LockUtil.lockPo(context, context.getRunLog(), logMapper);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            b = false;
        }
        if (!b) {
            interrupt(context, Response.error("获取乐观锁失败"));
            return;
        }
        context.setCasLock(true);
    }

    private void initLog(InputFlowContext context) {
        RunLog runLog = new RunLog();
        runLog.setCoreId(context.getCoreId());
        runLog.setSourceId(context.getInputDto().getSourceId());
        List<RunLog> logs = logMapper.selectByCondition(runLog);
        if (logs.isEmpty()) {
            runLog.setBusiDate(TimeUtil.now().toLocalDate());
            runLog.setActionId(context.getInputDto().getActionId());
            logMapper.insert(runLog);
            context.setRunLog(logMapper.selectById(runLog.getId(), RunLog.class));
            return;
        }
        RunLog runLog1 = logs.get(0);
        context.setRunLog(runLog1);
        // 状态校验
        if (runLog1.getStatus().intValue() >= CommonStatusEnum.SUCCESS.getCode()) {
            interrupt(context, Response.error("当前状态无需处理"));
        }
    }

    private void getCoreId(InputFlowContext context) {
        InputDto dto = context.getInputDto();
        RuleCoreCondition core = new RuleCoreCondition();
        core.setDomain(dto.getDomain());
        core.setBusiCode(dto.getBusiCode());
        core.setActionId(dto.getActionId());
        core.setSelectFiled(SqlConstants.ID);
        List<Long> ids = coreMapper.selectLongByCondition(core);
        context.setCoreId(ids.get(0));
    }

    private void getLock(InputFlowContext context) {
        RLock lock = LockUtil.getRedisLock(RedisUtil.getKey(SystemConstants.REDIS_KEY_PRE,LockScenarioConstants.INPUT,
                context.getCoreId().toString(), context.getInputDto().getSourceId().toString()));
        if (lock == null) {
            interrupt(context, Response.error("竞争redis锁失败"));
            return;
        }
        context.setLock(lock);
    }
}
