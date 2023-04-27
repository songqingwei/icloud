package cn.isqing.icloud.starter.variable.service.component.flow;

import cn.isqing.icloud.common.utils.constants.SqlConstants;
import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.common.utils.flow.FlowTemplate;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import cn.isqing.icloud.common.utils.time.TimeUtil;
import cn.isqing.icloud.common.utils.uuid.UuidUtil;
import cn.isqing.icloud.starter.variable.common.constants.CommonConfigGroupConstants;
import cn.isqing.icloud.starter.variable.common.constants.CommonTextTypeConstants;
import cn.isqing.icloud.starter.variable.common.constants.ComponentTextTypeConstants;
import cn.isqing.icloud.starter.variable.common.dto.ComponentExecDto;
import cn.isqing.icloud.starter.variable.dao.entity.*;
import cn.isqing.icloud.starter.variable.dao.mapper.CommonConfigMapper;
import cn.isqing.icloud.starter.variable.dao.mapper.CommonTextMapper;
import cn.isqing.icloud.starter.variable.dao.mapper.ComponentTextMapper;
import cn.isqing.icloud.starter.variable.service.component.ComponentExecService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Slf4j
public abstract class BaseComponentExecFlow extends FlowTemplate<ComponentExecContext, Object> implements ComponentExecService {

    @Autowired
    private ComponentTextMapper textMapper;

    @Autowired
    private CommonTextMapper commonTextMapper;

    @Autowired
    private CommonConfigMapper configMapper;

    // 提前组件结果的jsonPath分为三部分
    private Pattern cresPathPattern = Pattern.compile("^(\\$)\\.(\\d+)\\.([^\\s]+)$");

    protected BaseComponentExecFlow() {
        errorApply(this::errorAccept);
        stepName("查询组件配置");
        accept(this::getConfig);
        stepName("查询数据源配置");
        accept(this::getDSConfig);
        stepName("获取系统常量参数值");
        accept(this::getConstantValue);
        stepName("获取变量参数值");
        accept(this::getSystemVarsValue);
        stepName("准备执行");
        accept(this::pre);
        stepName("替换请求参数");
        accept(this::replaceRequestParam);
        stepName("执行组件");
        accept(this::execComponent);
        stepName("执行结果判断");
        accept(this::checkRes);
        stepName("将结果注册到上下文结果集");
        accept(this::registerRes);
    }

    private Response<Object> errorAccept(ComponentExecContext componentExecContext) {
        return Response.ERROR;
    }

    private void checkRes(ComponentExecContext context) {
        String[] resJudge = context.getResJudge();
        String res = context.getExecRes();
        for (int i = 0; i < resJudge.length; i++) {
            Object value = JsonUtil.extract(res, resJudge[i]);
            if (value == null) {
                interrupt(context, Response.error("执行结果异常"));
            }
        }
    }

    protected abstract void registerRes(ComponentExecContext context);

    private void replaceRequestParam(ComponentExecContext context) {
        // 替换模版参数
        resolveTplParams(context,context.getDependInputParams(), context.getExecDto().getInputParams());
        resolveTplDependCRes(context);
        resolveTplParams(context,context.getDependConstants(), context.getConstantsValue());
        resolveTplParams(context,context.getSelfConstants());
        resolveTplParams(context,context.getDependSystemVars(), context.getSystemVarsValue());
    }

    /**
     * 解析参数模版,遍历替换占位符
     *
     * @param context
     * @param config
     * @param inputParams
     */
    private void resolveTplParams(ComponentExecContext context, Map<String, String> config, String inputParams) {
        String[] tpl = context.getRequestParamsTpl();
        config.forEach((placeholder, jsonPath) -> {
            if (context.isInterrupted()) {
                return;
            }
            Object value = JSONPath.extract(inputParams, jsonPath);
            Response<Object> res = replace(tpl, placeholder, value);
            if (!res.isSuccess()) {
                interrupt(context, res);
            }
        });
    }

    private void resolveTplParams(ComponentExecContext context, Map<String, String> config) {
        String[] tpl = context.getRequestParamsTpl();
        config.forEach((placeholder, value) -> {
            if (context.isInterrupted()) {
                return;
            }
            Response<Object> res = replace(tpl, placeholder, value);
            if (!res.isSuccess()) {
                interrupt(context, res);
            }
        });
    }

    private void resolveTplParams(ComponentExecContext context, Map<String, String> config, Map<String, String> valueMap) {
        String[] tpl = context.getRequestParamsTpl();
        config.forEach((placeholder, valueMapkey) -> {
            if (context.isInterrupted()) {
                return;
            }
            String value = valueMap.get(valueMapkey);
            // todo-sqw 特殊类型转换
            Response<Object> res = replace(tpl, placeholder, value);
            if (!res.isSuccess()) {
                interrupt(context, res);
            }
        });
    }

    private void resolveTplDependCRes(ComponentExecContext context) {
        String[] tpl = context.getRequestParamsTpl();
        Map<String, String> config = context.getDependCRes();
        Map<Long, String> aboveResMap = context.getExecDto().getAboveResMap();
        config.forEach((placeholder, originalJsonPath) -> {
            if (context.isInterrupted()) {
                return;
            }
            Matcher matcher = cresPathPattern.matcher(originalJsonPath);
            if (!matcher.matches()) {
                interrupt(context, Response.error("DependCRes的JsonPath不规范,请重新配置"));
                return;
            }
            Object value = JSONPath.extract(aboveResMap.get(matcher.group(2)), "$."+matcher.group(3));
            Response<Object> res = replace(tpl, placeholder, value);
            if (!res.isSuccess()) {
                interrupt(context, res);
            }
        });
    }


    protected abstract Response<Object> replace(String[] requestParams, String path, Object value);

    protected abstract void pre(ComponentExecContext componentExecContext);

    private void getDSConfig(ComponentExecContext context) {
        Long dataSourceId = context.getComponent().getDataSourceId();
        CommonTextCondition text = new CommonTextCondition();
        text.setFid(dataSourceId);
        text.setType(CommonTextTypeConstants.DATA_SOURCE_CINFIG);
        text.setOrderBy(SqlConstants.ID_ASC);
        List<CommonText> texts = commonTextMapper.selectByCondition(text);
        String s = texts.stream().collect(Collectors.mapping(CommonText::getText, Collectors.joining()));
        Map<String, Object> configMap = JSONObject.parseObject(s, new TypeReference<Map<String, Object>>() {
        });
        context.setDsConfig(configMap);
    }

    protected abstract void execComponent(ComponentExecContext context);

    private void getSystemVarsValue(ComponentExecContext context) {
        Map<String, String> map = new HashMap<>();
        context.setSystemVarsValue(map);
        context.getDependSystemVars().values().forEach(v -> {
            switch (v) {
                case "uuid":
                    map.put(v, UuidUtil.randomNum_6());
                    break;
                case "nowDate":
                    map.put(v, TimeUtil.now().format(TimeUtil.dateFormatter));
                    break;
                case "nowTime":
                    map.put(v, TimeUtil.now().format(TimeUtil.dateTimeFormatter));
                    break;
                default:
                    map.put(v, null);
                    break;
            }
        });

    }

    private void getConstantValue(ComponentExecContext context) {
        List<String> keyList = new ArrayList<>(context.getConstantsValue().values());
        CommonConfigCondition config = new CommonConfigCondition();
        config.setGroup(CommonConfigGroupConstants.CONSTANTS);
        config.setKeyCondtion(keyList);
        config.setOrderBy(SqlConstants.ID_ASC);
        List<CommonConfig> list = configMapper.selectByCondition(config);
        Map<String, String> map = list.stream().collect(Collectors.groupingBy(CommonConfig::getKey,
                Collectors.mapping(CommonConfig::getValue, Collectors.joining())));
        context.setConstantsValue(map);
    }


    private void getConfig(ComponentExecContext context) {
        ComponentTextCondition condition = new ComponentTextCondition();
        condition.setFid(context.getComponent().getId());
        condition.setOrderBy(SqlConstants.ID_ASC);
        List<ComponentText> list = textMapper.selectByCondition(condition);
        Map<Integer, String> map = list.stream().collect(Collectors.groupingBy(ComponentText::getType,
                Collectors.mapping(ComponentText::getText, Collectors.joining())));

        context.setDialectConfig(map.get(ComponentTextTypeConstants.DIALECT_CONFIG));
        context.setDependInputParams(JSONObject.parseObject(map.get(ComponentTextTypeConstants.DEPEND_INPUT_PARAMS),
                new TypeReference<Map<String, String>>() {
                })
        );
        context.setDependCRes(JSONObject.parseObject(map.get(ComponentTextTypeConstants.DEPEND_C_RES),
                new TypeReference<Map<String, String>>() {
                })
        );
        context.setDependConstants(JSONObject.parseObject(map.get(ComponentTextTypeConstants.DEPEND_CONSTANTS),
                new TypeReference<Map<String, String>>() {
                })
        );
        context.setDependSystemVars(JSONObject.parseObject(map.get(ComponentTextTypeConstants.DEPEND_SYSTEM_VARS),
                new TypeReference<Map<String, String>>() {
                })
        );
        context.setSelfConstants(JSONObject.parseObject(map.get(ComponentTextTypeConstants.SELF_CONSTANTS),
                new TypeReference<Map<String, String>>() {
                })
        );
        context.setResJudge(JSON.parseArray(map.get(ComponentTextTypeConstants.RES_JUDGE)).toArray(new String[0]));
    }


    @Override
    public Response<Object> exec(Component component, ComponentExecDto dto) {
        ComponentExecContext context = new ComponentExecContext();
        context.setExecDto(dto);
        context.setComponent(component);
        return exec(context);
    }

}
