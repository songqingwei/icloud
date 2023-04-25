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
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    protected BaseComponentExecFlow() {
        errorApply(this::errorAccept);
        stepName("查询组件配置");
        accept(this::getConfig);
        stepName("查询数据源配置");
        accept(this::getDSConfig);
        stepName("获取常量参数值");
        accept(this::getConstantValue);
        stepName("获取变量参数值");
        accept(this::getVariableValue);
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

    private void checkRes(ComponentExecContext context){
        String resJudge = context.getResJudge();
        String res = context.getExecRes();
        Object value = JsonUtil.extract(res, resJudge);
        if(value==null){
            interrupt(context,Response.error("执行结果异常"));
        }
    }

    protected abstract void registerRes(ComponentExecContext context);

    private void replaceRequestParam(ComponentExecContext context) {
        String[] requestParams = context.getRequestParams();
        Map[][] arr = {
                {context.getConstantParams(),context.getConstantMap()},
                {context.getVariableParams(),context.getVariableMap()},
        };
        for (Map[] maps : arr) {
            if(maps[0]!=null){
                for (Object o : maps[0].entrySet()) {
                    Map.Entry<String,Object> entry = (Map.Entry) o;
                    Response<Object> res = replace(requestParams, entry.getKey(), maps[1].get(entry.getValue()));
                    if(!res.isSuccess()){
                        interrupt(context,res);
                        return;
                    }
                }
            }
        }
        // 原始入参解析
        if(context.getInputParams()!=null){
            for (Map.Entry<String, String> entry : context.getInputParams().entrySet()) {
                Response<Object> res = replace(requestParams, entry.getKey(), JSONPath.extract(context.getResDto().getInputParams(),entry.getValue()));
                if(!res.isSuccess()){
                    interrupt(context,res);
                    return;
                }
            }
        }
        // 上文结果集解析
        if(context.getAboveResParams()!=null){
            for (Map.Entry<String, String> entry : context.getAboveResParams().entrySet()) {
                String uni_path = entry.getValue();
                int index = uni_path.indexOf("_");
                Long cid = Long.valueOf(uni_path.substring(1, index));
                String jsonPath = uni_path.substring(index + 1);
                Response<Object> res = replace(requestParams, entry.getKey(), JSONPath.extract(context.getResDto().getAboveResMap().get(cid),jsonPath));
                if(!res.isSuccess()){
                    interrupt(context,res);
                    return;
                }
            }
        }
    }

    protected abstract Response<Object> replace(String[] requestParams,String path,Object value);

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

    private void getVariableValue(ComponentExecContext context) {
        Map<String, String> map = new HashMap<>();
        context.setVariableMap(map);
        context.getVariableParams().values().forEach(v -> {
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
        List<String> keyList = new ArrayList<>(context.getConstantParams().values());
        CommonConfigCondition config = new CommonConfigCondition();
        config.setGroup(CommonConfigGroupConstants.COMPONENT_PARAMS_CONSTANTS);
        config.setKeyCondtion(keyList);
        config.setOrderBy(SqlConstants.ID_ASC);
        List<CommonConfig> list = configMapper.selectByCondition(config);
        Map<String, String> map = list.stream().collect(Collectors.groupingBy(CommonConfig::getKey,
                Collectors.mapping(CommonConfig::getValue, Collectors.joining())));
        context.setConstantMap(map);
    }


    private void getConfig(ComponentExecContext context) {
        ComponentTextCondition condition = new ComponentTextCondition();
        condition.setFid(context.getComponent().getId());
        condition.setOrderBy(SqlConstants.ID_ASC);
        List<ComponentText> list = textMapper.selectByCondition(condition);
        Map<Integer, String> map = list.stream().collect(Collectors.groupingBy(ComponentText::getType,
                Collectors.mapping(ComponentText::getText, Collectors.joining())));

        context.setConstantParams(JSONObject.parseObject(map.get(ComponentTextTypeConstants.CONSTANT_PARAMS),
                new TypeReference<Map<String, String>>() {
                }));
        context.setVariableParams(JSONObject.parseObject(map.get(ComponentTextTypeConstants.VARIABLE_PARAMS),
                new TypeReference<Map<String, String>>() {
                }));
        context.setResJudge(map.get(ComponentTextTypeConstants.VARIABLE_PARAMS));

        context.setAboveResParams(JSONObject.parseObject(map.get(ComponentTextTypeConstants.ABOVE_RES_PARAMS),
                new TypeReference<Map<Long, String>>() {
                }));
        context.setInputParams(JSONObject.parseObject(map.get(ComponentTextTypeConstants.INPUT_PARAMS),
                new TypeReference<Map<Long, String>>() {
                }));
        context.setDialectConfig(map.get(ComponentTextTypeConstants.DIALECT_CONFIG));
    }


    @Override
    public Response<Object> exec(Component component, ComponentExecDto dto) {
        ComponentExecContext context = new ComponentExecContext();
        context.setResDto(dto);
        context.setComponent(component);
        return exec(context);
    }

}
