package cn.isqing.icloud.starter.variable.service.event.impl;

import cn.isqing.icloud.common.utils.annotation.RouteType;
import cn.isqing.icloud.common.utils.bean.SpringBeanUtils;
import cn.isqing.icloud.common.utils.constants.EventConstants;
import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.common.utils.enums.ResCodeEnum;
import cn.isqing.icloud.common.utils.flow.FlowTemplate;
import cn.isqing.icloud.starter.variable.api.dto.VariableSimpleDto;
import cn.isqing.icloud.starter.variable.common.constants.EventTypeConstants;
import cn.isqing.icloud.starter.variable.common.dto.ActuatorDto;
import cn.isqing.icloud.starter.variable.common.util.VariableCacheUtil;
import cn.isqing.icloud.starter.variable.dao.entity.Component;
import cn.isqing.icloud.starter.variable.dao.entity.Variable;
import cn.isqing.icloud.starter.variable.dao.entity.VariableCondition;
import cn.isqing.icloud.starter.variable.dao.entity.VariableFiled;
import cn.isqing.icloud.starter.variable.dao.mapper.VariableMapper;
import cn.isqing.icloud.starter.variable.service.component.flow.ComponentDigraphContext;
import cn.isqing.icloud.starter.variable.service.component.flow.ComponentDigraphFlow;
import cn.isqing.icloud.starter.variable.service.event.EventSubscriber;
import cn.isqing.icloud.starter.variable.service.msg.dto.EventMsg;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
@Slf4j
@RouteType(r1 = EventTypeConstants.VSET_CHANGE, r2 = EventConstants.BROADCASTING_MODEL)
public class VsetChangeFlow extends FlowTemplate<VsetChangeContext, Object> implements EventSubscriber {

    @Autowired
    private ComponentDigraphFlow digraphFlow;
    @Autowired
    private VariableMapper mapper;

    /**
     * coreId,threadId
     */
    private final Map<String, Long> lockMap = new ConcurrentHashMap<>();

    public VsetChangeFlow() {
        start("变量：规则模版变更流程", this);
        stepName("解析消息");
        test(c->c.getVidList()==null);
        accept(this::parseMsg);
        stepName("获取单机锁");
        accept(this::getLock);
        stepName("获取变量信息");
        accept(this::getVarInfo);
        stepName("获取变量对应组件拓扑图");
        accept(this::parseVariables);
        finallyAcceptName("释放资源");
        finallyAccept(this::releaseResource);
    }

    private void getVarInfo(VsetChangeContext context) {
        VariableCondition condition = new VariableCondition();
        condition.setCidCondition(context.getVidList());
        condition.setSelectFiled(VariableFiled.ID, VariableFiled.CID, VariableFiled.C_RES_PATH);
        List<Variable> list = mapper.selectByCondition(condition);
        Map<Long, VariableSimpleDto> map = list.stream().collect(Collectors.toMap(v -> v.getId(), v -> {
            VariableSimpleDto simpleDto = new VariableSimpleDto();
            SpringBeanUtils.copyProperties(v, simpleDto);
            return simpleDto;
        }, (key1, key2) -> key1));
        context.setVariableMap(map);
    }

    private void parseMsg(VsetChangeContext context) {
        List<String> list = context.getEventMsg().getData();
        List<Long> vidList = JSONObject.parseObject(list.get(0), new TypeReference<List<Long>>() {
        });
        context.setVidList(vidList);
    }

    private void releaseResource(VsetChangeContext context) {
        long threadId = Thread.currentThread().getId();
        String id = context.getEventMsg().getId();
        Long lastTid = lockMap.get(id);
        if (lastTid == null) {
            return;
        }
        if (lastTid.equals(threadId)) {
            lockMap.remove(lastTid);
        }

    }


    private void parseVariables(VsetChangeContext context) {
        // 获取组件id
        List<Long> list =
                context.getVariableMap().entrySet().stream().map(e -> e.getValue().getCid()).distinct().collect(Collectors.toList());

        ComponentDigraphContext digraphContext = new ComponentDigraphContext();
        digraphContext.setCidReq(list);
        Response<List<List<Component>>> res = digraphFlow.exec(digraphContext);
        if (!res.isSuccess()) {
            log.error(res.getMsg());
            interrupt(context, Response.error("解析组件拓扑图异常"));
            return;
        }
        ActuatorDto actuatorDto = new ActuatorDto();
        actuatorDto.setComponentList(res.getData());
        actuatorDto.setVariableMap(context.getVariableMap());
        VariableCacheUtil.actuatorMap.put(context.getEventMsg().getId(), actuatorDto);
    }

    private void getLock(VsetChangeContext context) {
        long threadId = Thread.currentThread().getId();
        lockMap.put(context.getEventMsg().getId(), threadId);
        try {
            Thread.sleep(3 * 1000);
        } catch (Exception e) {
            log.warn("睡眠异常:{}", e.getMessage(), e);
            e.printStackTrace();
            interrupt(context, Response.error("获取单机锁:睡眠异常"));
            return;
        }
        Long lastThreadId = lockMap.get(context.getEventMsg().getId());
        if (lastThreadId == null) {
            interrupt(context, Response.error("获取单机锁失败，稍后重试"));
            return;
        }
        if (!lastThreadId.equals(threadId)) {
            interrupt(context, Response.info(ResCodeEnum.CANCEL.getCode(), "获取单机锁失败，取消本次处理"));
            return;
        }
    }

    @Override
    public void onEvent(EventMsg eventMsg) {
        VsetChangeContext context = new VsetChangeContext();
        context.setEventMsg(eventMsg);
        exec(context);
    }
}
