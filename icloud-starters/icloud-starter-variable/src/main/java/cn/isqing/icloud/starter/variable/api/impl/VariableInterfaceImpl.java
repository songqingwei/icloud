package cn.isqing.icloud.starter.variable.api.impl;

import cn.isqing.icloud.common.utils.bean.SpringBeanUtils;
import cn.isqing.icloud.common.utils.constants.SqlConstants;
import cn.isqing.icloud.common.utils.dto.BaseException;
import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.kit.ParallelStreamUtil;
import cn.isqing.icloud.common.utils.kit.StrUtil;
import cn.isqing.icloud.starter.variable.api.VariableInterface;
import cn.isqing.icloud.starter.variable.api.dto.*;
import cn.isqing.icloud.starter.variable.api.util.VariableUtil;
import cn.isqing.icloud.starter.variable.common.constants.CommonConfigGroupConstants;
import cn.isqing.icloud.starter.variable.common.constants.CommonTextTypeConstants;
import cn.isqing.icloud.starter.variable.common.constants.EventTypeConstants;
import cn.isqing.icloud.starter.variable.common.dto.ActuatorDto;
import cn.isqing.icloud.starter.variable.common.dto.ComponentExecDto;
import cn.isqing.icloud.starter.variable.common.dto.common.config.VsetDefQueryConf;
import cn.isqing.icloud.starter.variable.common.util.VariableCacheUtil;
import cn.isqing.icloud.starter.variable.dao.entity.*;
import cn.isqing.icloud.starter.variable.dao.mapper.CommonConfigMapper;
import cn.isqing.icloud.starter.variable.dao.mapper.CommonTextMapper;
import cn.isqing.icloud.starter.variable.dao.mapper.ComponentMapper;
import cn.isqing.icloud.starter.variable.dao.mapper.VariableMapper;
import cn.isqing.icloud.starter.variable.service.component.ComponentExecService;
import cn.isqing.icloud.starter.variable.service.component.factory.ComponentExecFactory;
import cn.isqing.icloud.starter.variable.service.event.EventPublisher;
import cn.isqing.icloud.starter.variable.service.event.impl.VsetChangeContext;
import cn.isqing.icloud.starter.variable.service.event.impl.VsetChangeFlow;
import cn.isqing.icloud.starter.variable.service.msg.dto.EventMsg;
import cn.isqing.icloud.starter.variable.service.variable.VariableService;
import cn.isqing.icloud.starter.variable.service.variable.dto.VariableDto;
import cn.isqing.icloud.starter.variable.service.variable.dto.VariableListReq;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service(group = "${i.variable.dubbo.group:iVariable}", timeout = 60000, retries = -1, version = "1.0.0")
//@Service
@Slf4j
public class VariableInterfaceImpl implements VariableInterface {

    @Value("${i.variable.execCompoentTimeOut:60000}")
    private int execCompoentTimeOut;
    @Autowired
    private VariableMapper mapper;
    @Autowired
    private CommonTextMapper textMapper;
    @Autowired
    private VariableService service;
    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private ComponentExecFactory execFactory;
    @Autowired
    private ComponentMapper componentMapper;
    @Autowired
    private VsetChangeFlow vsetChangeFlow;
    @Autowired
    private CommonConfigMapper configMapper;

    @Override
    public Response<Object> publishVsetChangeEvent(String coreId, List<Long> list) {
        eventPublisher.publishBcEvent(coreId, EventTypeConstants.VSET_CHANGE, list);
        return Response.SUCCESS;
    }

    @Override
    public Response<Map<Long, String>> getComponentRes(ApiVariablesValueReqDtoApi reqDto) {
        Response<ActuatorDto> actuatorRes = getActuatorDto(reqDto);
        if (!actuatorRes.isSuccess()) {
            return Response.withData(actuatorRes, null);
        }
        ActuatorDto actuatorDto = actuatorRes.getData();
        List<List<Component>> componentList = actuatorDto.getComponentList();
        // 组件结果集
        Map<Long, String> resMap = new ConcurrentHashMap<>();
        if (reqDto.getAboveResMap() != null && !reqDto.getAboveResMap().isEmpty()) {
            resMap.putAll(reqDto.getAboveResMap());
        }
        ComponentExecDto resDto = new ComponentExecDto();
        resDto.setAboveResMap(resMap);
        resDto.setInputParams(reqDto.getInputParams());
        AtomicBoolean end = new AtomicBoolean(false);
        AtomicInteger total = new AtomicInteger(0);
        AtomicBoolean hasError = new AtomicBoolean(false);
        Consumer<Component> consumer = (c) -> {
            if (resMap.get(c.getId()) != null) {
                total.incrementAndGet();
                return;
            }
            ComponentExecService service = execFactory.getSingle(c.getDataSourceType().toString());
            Response<Object> res = service.exec(c, resDto);
            if (!res.isSuccess()) {
                log.error(res.getMsg());
                hasError.set(true);
            } else {
                total.incrementAndGet();
            }
        };

        componentList.forEach(list -> {
            if (end.get()) {
                return;
            }
            try {
                ParallelStreamUtil.exec(list, consumer, execCompoentTimeOut);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            // 判断整个层级是否都失败了
            if (total.intValue() == 0) {
                end.set(true);
            }
        });
        if (hasError.get() || end.get()) {
            return Response.error("获取变量值异常", resDto.getAboveResMap());
        }
        return Response.success(resDto.getAboveResMap());
    }

    /**
     * 获取执行器dto
     *
     * @param reqDto
     * @return
     */
    private Response<ActuatorDto> getActuatorDto(ApiVariablesValueReqDtoApi reqDto) {
        ActuatorDto actuatorDto = VariableCacheUtil.actuatorMap.get(reqDto.getCoreId());
        if (actuatorDto != null) {
            return Response.success(actuatorDto);
        }
        // 获取配置
        VsetDefQueryConf conf = getCommonConfig(reqDto);
        // 获取组件
        Component c = componentMapper.selectById(conf.getCid(), Component.class);
        if (c == null) {
            return Response.error("缺少组件配置,请联系管理员");
        }
        // 执行组件获取变量id
        List<Long> vidList = getVidList(reqDto, conf, c);
        // 变更事件
        vsetChangeEvent(reqDto, vidList);
        actuatorDto = VariableCacheUtil.actuatorMap.get(reqDto.getCoreId());
        return Response.success(actuatorDto);
    }

    private List<Long> getVidList(ApiVariablesValueReqDtoApi reqDto, VsetDefQueryConf conf, Component c) {
        ComponentExecDto resDto = new ComponentExecDto();
        resDto.setInputParams(reqDto.getInputParams());
        ComponentExecService service = execFactory.getSingle(c.getDataSourceType().toString());
        Response<Object> res = service.exec(c, resDto);
        if (!res.isSuccess()) {
            log.error(res.getMsg());
            throw new BaseException("查询失败");
        }
        Object value = JSONPath.extract(resDto.getAboveResMap().get(conf.getCid()), conf.getCresPath());
        return JSONArray.parseArray(value.toString()).toList(Long.class);
    }

    private VsetDefQueryConf getCommonConfig(ApiVariablesValueReqDtoApi reqDto) {
        CommonConfig config = new CommonConfig();
        config.setGroup(StrUtil.assembleKey(CommonConfigGroupConstants.VSET_DEFINITION_QUERY, reqDto.getDomain().toString()));
        config.setKey(reqDto.getCoreId().toString());
        CommonConfig first = configMapper.first(config, null);
        if (first == null) {
            throw new BaseException("缺少变量集查询配置,请联系管理员");
        }
        return JSON.parseObject(first.getValue(), VsetDefQueryConf.class);
    }

    private void vsetChangeEvent(ApiVariablesValueReqDtoApi reqDto, List<Long> vidList) {
        VsetChangeContext context = new VsetChangeContext();
        EventMsg eventMsg = new EventMsg();
        context.setEventMsg(eventMsg);
        eventMsg.setId(reqDto.getCoreId());
        context.setVidList(vidList);
        Response<Object> res = vsetChangeFlow.exec(context);
        if (!res.isSuccess()) {
            log.warn(res.getMsg());
            throw new BaseException("缓存组件异常");
        }
    }


    @Override
    public Response<Map<Long, Object>> getValues(ApiVariablesValueReqDtoApi reqDto) {
        Response<Map<Long, String>> res = getComponentRes(reqDto);
        ActuatorDto actuatorDto = VariableCacheUtil.actuatorMap.get(reqDto.getCoreId());
        if (actuatorDto == null) {
            return Response.error("系统繁忙");
        }
        Map<Long, String> data = res.getData();

        Map<Long, Object> map = new HashMap<>();
        actuatorDto.getVariableMap().forEach((k, v) -> {
            map.put(k, VariableUtil.getValue(v, data));
        });
        if (!res.isSuccess()) {
            return Response.info(res.getCode(), res.getMsg(), map);
        }
        return Response.success(map);
    }

    @Override
    public Response<ApiVariableDto> getVariableById(Long id) {
        Variable variable = mapper.selectById(id, Variable.class);
        ApiVariableDto dto = new ApiVariableDto();
        SpringBeanUtils.copyProperties(variable, dto);
        return Response.success(dto);
    }

    @Override
    public Response<ApiVaroablePageResDto<ApiVariableDto>> list(PageReqDto<ApiVariableListReq> req) {
        VariableListReq condition = new VariableListReq();
        PageReqDto<VariableListReq> reqDto = new PageReqDto<>();
        reqDto.setCondition(condition);
        reqDto.setPageInfo(req.getPageInfo());
        SpringBeanUtils.copyProperties(req.getCondition(), condition);

        Response<PageResDto<VariableDto>> rawRes;
        if (req.getCondition().getActionId() != null) {
            rawRes = service.listWithAction(reqDto);
        } else {
            rawRes = service.listNoAction(reqDto);
        }
        if (!rawRes.isSuccess()) {
            return Response.info(rawRes.getCode(), rawRes.getMsg());
        }
        List<VariableDto> dtoList = rawRes.getData().getList();

        Response<ApiVaroablePageResDto<ApiVariableDto>> res = Response.success(new ApiVaroablePageResDto<>());
        PageResDto<ApiVariableDto> data = res.getData();

        data.setTotal(res.getData().getTotal());
        if (dtoList != null) {
            Set<Long> set = new HashSet<>();
            List<ApiVariableDto> list = dtoList.stream().map(o -> {
                ApiVariableDto dto = new ApiVariableDto();
                SpringBeanUtils.copyProperties(o, dto);
                if (o.getRendererId().longValue() > 0) {
                    set.add(o.getRendererId());
                }
                return dto;
            }).collect(Collectors.toList());
            data.setList(list);
            setRenderer(res, set);
        }
        return res;

    }

    /**
     * 查询渲染器
     *
     * @param res
     * @param set
     */
    private void setRenderer(Response<ApiVaroablePageResDto<ApiVariableDto>> res, Set<Long> set) {
        CommonTextCondition condition = new CommonTextCondition();
        condition.setFidCondition(new ArrayList<>(set));
        condition.setType(CommonTextTypeConstants.RENDERER);
        condition.setOrderBy(SqlConstants.ID_ASC);
        List<CommonText> texts = textMapper.selectByCondition(condition);
        Map<Long, Map<String, Object>> renderer = texts.stream().collect(Collectors.groupingBy(CommonText::getFid, Collectors.mapping(CommonText::getText,
                Collectors.collectingAndThen(Collectors.joining(), text -> JSON.parseObject(text, new TypeReference<Map<String, Object>>() {})))));
        res.getData().setRenderer(renderer);

    }
}
