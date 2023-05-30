package cn.isqing.icloud.starter.drools.service.event.impl;

import cn.hutool.core.util.StrUtil;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.annotation.RouteType;
import cn.isqing.icloud.common.utils.bean.SpringBeanUtils;
import cn.isqing.icloud.common.utils.constants.EventConstants;
import cn.isqing.icloud.common.utils.constants.SqlConstants;
import cn.isqing.icloud.common.utils.dto.BaseException;
import cn.isqing.icloud.common.utils.flow.FlowTemplate;
import cn.isqing.icloud.common.utils.kit.LockUtil;
import cn.isqing.icloud.common.utils.kit.RedisUtil;
import cn.isqing.icloud.common.utils.time.TimeUtil;
import cn.isqing.icloud.starter.drools.common.constants.*;
import cn.isqing.icloud.starter.drools.common.dto.RuleKeyDto;
import cn.isqing.icloud.starter.drools.common.enums.AlgorithModel;
import cn.isqing.icloud.starter.drools.common.enums.AllocationModel;
import cn.isqing.icloud.starter.drools.common.util.ComponentUtil;
import cn.isqing.icloud.starter.drools.common.util.KieUtil;
import cn.isqing.icloud.starter.drools.dao.entity.*;
import cn.isqing.icloud.starter.drools.dao.mapper.*;
import cn.isqing.icloud.starter.drools.service.component.flow.ComponentDigraphContext;
import cn.isqing.icloud.starter.drools.service.component.flow.ComponentDigraphFlow;
import cn.isqing.icloud.starter.drools.service.event.EventSubscriber;
import cn.isqing.icloud.starter.drools.service.msg.MsgParserService;
import cn.isqing.icloud.starter.drools.service.msg.dto.EventMsg;
import cn.isqing.icloud.starter.drools.service.msg.dto.TplChangeMsg;
import cn.isqing.icloud.starter.drools.service.semaphore.dto.AllotterConfigDto;
import cn.isqing.icloud.starter.drools.service.semaphore.util.Allotter;
import cn.isqing.icloud.starter.variable.api.VariableInterface;
import cn.isqing.icloud.starter.variable.api.dto.ApiVariableDto;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.internal.conf.ConstraintJittingThresholdOption;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 模版变更应该抛出异常，等待消息重试
 * 即不能定义模版error方法
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
@Service
@RouteType(r1 = EventTypeConstants.TPL_CHANGE, r2 = EventConstants.BROADCASTING_MODEL)
public class RuleTemplateChangeFlow extends FlowTemplate<RuleTemplateChangeContext, Object> implements EventSubscriber {

    @Autowired
    private RuleTemplateBusiMapper busiMapper;
    @Autowired
    private RuleCoreMapper coreMapper;
    @Autowired
    private CommonTextMapper textMapper;
    @Autowired
    private ActionMapper actionMapper;
    @Autowired
    private MsgParserService msgParserService;
    private KieServices kieServices = KieServices.Factory.get();
    @Autowired
    private ComponentDigraphFlow digraphFlow;
    @Autowired
    private ComponentTextMapper componentTextMapper;
    @Reference(group = "${i.variable.dubbo.group:iVariable}", timeout = 60000, retries = -1, version = "1.0.0")
    private VariableInterface variableInterface;
    @Autowired
    private RunCoreTextMapper runCoreTextMapper;

    private int limit = 100;


    private final Map<TplChangeMsg, LocalDateTime> msgMap = new ConcurrentHashMap<>();

    public RuleTemplateChangeFlow() {
        start("规则变更流程", this);
        stepName("解析消息");
        accept(this::parseMsg);
        stepName("获取并发锁");
        accept(this::getLock);
        stepName("限界表检测");
        accept(this::checkRuleCore);
        stepName("分页解析规则");
        accept(this::dealRecords);
        stepName("获取action拓扑图");
        accept(this::parseAction);
        stepName("获取action依赖变量");
        accept(this::getActionVar);
        stepName("记录变量集");
        accept(this::recordVset);
        stepName("发布事件通知变量服务");
        accept(this::publishEvent);
        finallyAcceptName("释放资源");
        finallyAccept(this::release);
    }

    private void release(RuleTemplateChangeContext context) {
        if(context.isLock()){
            TplChangeMsg msg = context.getMsg();
            msgMap.remove(msg, context.getDealTime());
        }
    }

    private void recordVset(RuleTemplateChangeContext context) {
        String key = RedisUtil.getKey(SystemConstants.REDIS_KEY_PRE, LockScenarioConstants.RECORD_VSET_IDS, context.getCore().getId().toString());

        Consumer consumer = data -> {
            RunCoreText text = new RunCoreText();
            text.setFid(context.getCore().getId());
            text.setType(RunCoreTextTypeConstants.VIDLIST);
            List<Long> list = context.getVariableMap().entrySet().stream().map(e -> e.getValue().getId()).collect(Collectors.toList());
            String[] strings = JSON.toJSONString(list).split(TextConstants.REGEX_5000);
            runCoreTextMapper.del(text);
            for (String string : strings) {
                text.setId(null);
                text.setText(string);
                runCoreTextMapper.insert(text);
            }
        };
        LockUtil.tryRunWithRLock(key, 5, TimeUnit.SECONDS, data -> true, consumer);
    }

    private void getActionVar(RuleTemplateChangeContext context) {
        List<List<Component>> list = KieUtil.actionMap.get(context.getRuleKeyDto());
        ComponentTextCondition condition1 = new ComponentTextCondition();
        condition1.setType(ComponentTextTypeConstants.DEPEND_VARIABLES);
        condition1.setOrderBy(SqlConstants.ID_ASC);
        List<Long> cids = new ArrayList<>();

        list.forEach(innerList -> innerList.forEach(c -> {
            condition1.setFid(c.getId());
            List<ComponentText> componentTexts = componentTextMapper.selectByCondition(condition1);
            if(componentTexts.isEmpty()){
                return;
            }
            String s = componentTexts.stream().map(ComponentText::getText).collect(Collectors.joining());
            cids.addAll(JSON.parseObject(s, new TypeReference<List<Long>>() {
            }));
        }));
        context.setActionDepandCids(cids);
    }

    /**
     * 当变量集合整个删除的时候，没有通知变量组件刷新内存
     *
     * @param context
     */
    private void publishEvent(RuleTemplateChangeContext context) {
        List<Long> list = context.getVariableMap().entrySet().stream().map(e -> e.getValue().getId()).collect(Collectors.toList());
        variableInterface.publishVsetChangeEvent(context.getCore().getId().toString(), list);
        if(!context.getActionDepandCids().isEmpty()) {
            variableInterface.publishVsetChangeEvent(ComponentUtil.getActionCoreId(context.getCore().getId()), context.getActionDepandCids());
        }
    }

    private void parseAction(RuleTemplateChangeContext context) {
        Long actionId = context.getRuleKeyDto().getActionId();
        Action action = actionMapper.selectById(actionId, Action.class);
        ComponentDigraphContext digraphContext = new ComponentDigraphContext();
        digraphContext.setCidReq(Arrays.asList(action.getCid()));
        Response<List<List<Component>>> res = digraphFlow.exec(digraphContext);
        if (!res.isSuccess()) {
            log.error(res.getMsg());
            interrupt(context, Response.error("解析action组件拓扑图异常"));
            return;
        }
        KieUtil.actionMap.put(context.getRuleKeyDto(), res.getData());
    }

    private void checkRuleCore(RuleTemplateChangeContext context) {
        RuleKeyDto ruleKeyDto = context.getRuleKeyDto();
        RuleCore core = new RuleCore();
        SpringBeanUtils.copyProperties(ruleKeyDto, core);

        String key = RedisUtil.getKey(SystemConstants.REDIS_KEY_PRE, LockScenarioConstants.INSERT_CORE, ruleKeyDto.getDomain().toString(), ruleKeyDto.getBusiCode(), ruleKeyDto.getActionId().toString());
        Predicate predicate = data -> {
            RuleCore ruleCore = coreMapper.first(core, null);
            context.setCore(ruleCore);
            return ruleCore == null;
        };
        Consumer consumer = data -> {
            coreMapper.insert(core);
            context.setCore(core);
        };

        LockUtil.tryRunWithRLock(key, 5, TimeUnit.SECONDS, predicate, consumer);
        if (context.getCore() == null) {
            interrupt(context, Response.error("获取限界记录失败"));
        }

    }

    private List<CommonText> getTexts(List<Long> fids) {
        // 查询
        CommonTextCondition condition = new CommonTextCondition();
        condition.setFidCondition(fids);
        condition.setTypeCondition(Arrays.asList(CommonTextTypeConstants.RULE_CONTENT,
                CommonTextTypeConstants.TARGET_RATIO,
                CommonTextTypeConstants.TARGET_NAME,
                CommonTextTypeConstants.RULE_VARIABLE_MAP));
        condition.setOrderBy(SqlConstants.ID_ASC);
        List<CommonText> texts = textMapper.selectByCondition(condition);
        return texts;
    }

    private void dealRecords(RuleTemplateChangeContext context) {
        KieUtil.helperMap.remove(context.getRuleKeyDto());
        KieHelper kieHelper = KieUtil.getKieHelper(context.getRuleKeyDto());
        RuleKeyDto ruleKeyDto = context.getRuleKeyDto();
        Map<String, ApiVariableDto> map = new HashMap<>();
        long from = 0;
        Allotter.romoveConfig(context.getCore().getId());
        do {
            List<RuleTemplate> list = busiMapper.getScrollListByTplChangeMsg(context.getMsg(), from,
                    limit, SqlConstants.ID_ASC);
            if (list.isEmpty()) {
                break;
            }
            from = list.get(list.size() - 1).getId();
            List<Long> fids = list.stream().map(RuleTemplate::getId).collect(Collectors.toList());
            List<CommonText> texts = getTexts(fids);
            // 计数器规则缓存
            cacheSemaphoreConfig(context, list, texts);
            Map<Integer, Map<Long, String>> collect = getRuleAndVar(texts);
            // 填充变量集合
            collect.get(CommonTextTypeConstants.RULE_VARIABLE_MAP).values().forEach(s -> {
                map.putAll(JSON.parseObject(s, new TypeReference<Map<String, ApiVariableDto>>() {
                }));
            });

            String drl = KieUtil.getDrl(ruleKeyDto, list, collect.get(CommonTextTypeConstants.RULE_CONTENT));
            log.info(".drl:{}",drl);
            byte[] b1 = drl.getBytes();
            Resource resource = kieServices.getResources().newByteArrayResource(b1);
            kieHelper.addResource(resource, ResourceType.DRL);
        } while (true);

        // 如果查不到配置则清理内存
        if (map.isEmpty()) {
            KieUtil.baseMap.put(ruleKeyDto, null);
            KieUtil.variableMap.put(ruleKeyDto, null);
            KieUtil.actionMap.put(ruleKeyDto, null);
            interrupt(context, Response.success("清理内存"));
            return;
        }

        Set<String> set = map.entrySet().stream().map(e -> e.getKey()+": "+e.getValue().getTypePath()).collect(Collectors.toSet());

        String dataStr = StrUtil.format(FactDataConstants.DRL_TEMPLATE, set.stream().collect(Collectors.joining("\n")));
        log.info(".drl:{}",dataStr);
        Resource resource = kieServices.getResources().newByteArrayResource(dataStr.getBytes());
        kieHelper.addResource(resource, ResourceType.DRL);
        KieBaseConfiguration kieBaseConfiguration = kieServices.newKieBaseConfiguration();
        //禁用jittingThreshold(阈值默认20，设为-1)
        kieBaseConfiguration.setOption(ConstraintJittingThresholdOption.get(-1));
        KieBase kieBase = kieHelper.build(kieBaseConfiguration);

        KieUtil.baseMap.put(ruleKeyDto, kieBase);
        context.setVariableMap(map);
        KieUtil.variableMap.put(ruleKeyDto, map);
    }

    private void cacheSemaphoreConfig(RuleTemplateChangeContext context, List<RuleTemplate> list,
                                      List<CommonText> texts) {
        Map<Long, Map<Integer, String>> collect1 =
                texts.stream().filter(c -> c.getType().equals(CommonTextTypeConstants.TARGET_NAME) || c.getType().equals(CommonTextTypeConstants.TARGET_RATIO)).collect(Collectors.groupingBy(CommonText::getFid, Collectors.groupingBy(CommonText::getType, Collectors.mapping(CommonText::getText,
                        Collectors.joining()))));
        Long coreId = context.getCore().getId();
        list.forEach(t -> {
            AllotterConfigDto configDto = new AllotterConfigDto();
            configDto.setCoreId(coreId);
            configDto.setRid(t.getId());
            configDto.setAllocationModel(AllocationModel.getEnum(t.getAllocationModel()));
            if (t.getRefId().longValue()==0L) {
                configDto.setAlgorithModel(AlgorithModel.RANDOM);
            } else {
                configDto.setAlgorithModel(AlgorithModel.S_SHAPED);
            }
            Map<Integer, String> map = collect1.get(t.getId());
            configDto.setTargetNames(JSONObject.parseObject(map.get(CommonTextTypeConstants.TARGET_NAME),
                    new TypeReference<Map<Long, String>>() {
                    }));
            configDto.setAllotMap(JSONObject.parseObject(map.get(CommonTextTypeConstants.TARGET_RATIO),
                    new TypeReference<Map<Long, String>>() {
                    }));
            configDto.setTargetIds(new ArrayList<>(configDto.getAllotMap().keySet()));
            Allotter.addConfig(coreId, t.getId(), configDto);
        });
    }

    private Map<Integer, Map<Long, String>> getRuleAndVar(List<CommonText> texts) {
        Map<Integer, Map<Long, String>> collect =
                texts.stream().filter(c -> c.getType().equals(CommonTextTypeConstants.RULE_CONTENT)
                        || c.getType().equals(CommonTextTypeConstants.RULE_VARIABLE_MAP)
                ).collect(Collectors.groupingBy(CommonText::getType,
                        Collectors.groupingBy(CommonText::getFid, Collectors.mapping(CommonText::getText,
                                Collectors.joining()))));
        return collect;
    }

    private void getLock(RuleTemplateChangeContext context) {
        TplChangeMsg msg = context.getMsg();

        LocalDateTime createTime = msg.getCreateTime();
        // put map时忽略消息生产时间，按业务属性操作，处理时间延后5秒 解决5秒内的并发操作
        msg.setCreateTime(null);

        LocalDateTime dealTime = TimeUtil.now().plusSeconds(5L);
        LocalDateTime old = msgMap.putIfAbsent(msg, dealTime);
        if (old == null) {
            context.setLock(true);
            context.setDealTime(dealTime);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Thread.interrupted();
                throw new BaseException("时间延迟处理异常");
            }
            return;
        }
        if (old.isAfter(createTime) || old.isAfter(dealTime)) {
            log.info("消息失效，本次取消");
            context.setInterrupted(true);
            return;
        } else {
            context.setInterrupted(true);
            String tip = "处理中，稍后重试";
            log.info(tip);
            throw new BaseException(tip);
        }
    }

    private void parseMsg(RuleTemplateChangeContext context) {
        String msgReq = context.getMsgReq();
        TplChangeMsg msg = msgParserService.parseTplChangeEventMsg(msgReq);
        context.setMsg(msg);
        RuleKeyDto ruleKeyDto = new RuleKeyDto();
        ruleKeyDto.setBusiCode(msg.getBusiCode());
        ruleKeyDto.setDomain(msg.getDomain());
        ruleKeyDto.setActionId(msg.getActionId());
        context.setRuleKeyDto(ruleKeyDto);
    }

    @Override
    public void onEvent(EventMsg eventMsg) {
        String s = eventMsg.getData().get(0);
        RuleTemplateChangeContext context = new RuleTemplateChangeContext();
        context.setMsgReq(s);
        exec(context);
    }
}
