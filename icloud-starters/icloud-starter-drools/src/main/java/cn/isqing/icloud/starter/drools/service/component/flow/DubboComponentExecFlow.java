package cn.isqing.icloud.starter.drools.service.component.flow;

import cn.isqing.icloud.common.utils.annotation.RouteType;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import cn.isqing.icloud.starter.drools.common.constants.DataSourceTypeConstatnts;
import cn.isqing.icloud.starter.drools.common.constants.DubboDSConfigConstatnts;
import cn.isqing.icloud.starter.drools.common.dto.ComponentExecDto;
import cn.isqing.icloud.starter.drools.common.dto.DubboMethodDto;
import cn.isqing.icloud.starter.drools.common.enums.DubboComponentDialectType;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
@Slf4j
@RouteType(r1 = DataSourceTypeConstatnts.DUBBO)
public class DubboComponentExecFlow extends BaseComponentExecFlow {

    @Override
    protected void registerRes(ComponentExecContext context) {
        ComponentExecDto resDto = context.getExecDto();
        resDto.getAboveResMap().put(context.getComponent().getId(), context.getExecRes());
    }

    @Override
    protected Response<Object> replace(String[] requestParams, String path, Object value) {
        String v = String.valueOf(value);
        if (path.startsWith("##{")) {
            v = "\"" + v + "\"";
        } else if(path.startsWith("#{")) {
            v = "\'" + v + "\'";
        }
        requestParams[0] = requestParams[0].replace("#{" + path + "}", v);

        return Response.SUCCESS;
    }

    @Override
    protected void pre(ComponentExecContext context) {
        // 数据源
        Map<String, Object> dsConfig = context.getDsConfig();
        String address = (String) dsConfig.get(DubboDSConfigConstatnts.ADDRESS);
        DubboMethodDto methodDto = new DubboMethodDto();
        methodDto.setAddress(address);
        String config = context.getDialectConfig();

        methodDto.setInterfaceName((String) JsonUtil.extract(config,
                DubboComponentDialectType.INTERFACENAME.getJsonPath()));
        methodDto.setMethodName((String) JsonUtil.extract(config, DubboComponentDialectType.METHOD_NAME.getJsonPath()));
        methodDto.setMethodType((String[]) JsonUtil.extract(config,
                DubboComponentDialectType.METHOD_TYPE.getJsonPath()));
        methodDto.setGroup((String) JsonUtil.extract(config, DubboComponentDialectType.GROUP.getJsonPath()));
        methodDto.setVersion((String) JsonUtil.extract(config, DubboComponentDialectType.VERSION.getJsonPath()));
        context.setRequestDto(methodDto);

        String params = (String) JsonUtil.extract(config, DubboComponentDialectType.PARAMS.getJsonPath());
        context.setRequestParamsTpl(new String[]{params});
    }

    @Override
    protected void execComponent(ComponentExecContext context) {
        DubboMethodDto dto = (DubboMethodDto) context.getRequestDto();
        JSONArray objects = JSONArray.parseArray(context.getRequestParamsTpl()[0]);
        dto.setParamObj(objects.toArray());
        ReferenceConfig<GenericService> referenceConfig = getReferenceConfig(dto);
        try {
            // Apache Dubbo 2.7+ 内置缓存机制，直接调用get()即可
            GenericService genericService = referenceConfig.get();
            Object res = genericService.$invoke(dto.getMethodName(), dto.getMethodType(), dto.getParamObj());
            if (res instanceof String) {
                context.setExecRes((String) res);
            } else {
                context.setExecRes(JSON.toJSONString(res));
            }
        } catch (Exception e) {
            referenceConfig.destroy();
            log.error(e.getMessage(), e);
            interrupt(context, Response.error("调用dubbo接口异常"));
            return;
        }
    }

    private ReferenceConfig<GenericService> getReferenceConfig(DubboMethodDto dubboMethodDto) {
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setInterface(dubboMethodDto.getInterfaceName());
        reference.setCheck(false);
        reference.setRetries(-1);
        reference.setGeneric("true");
        reference.setTimeout(10 * 1000);
        if (dubboMethodDto.getMethodName().startsWith("dubbo")) {
            reference.setUrl(dubboMethodDto.getAddress());
        } else {
            RegistryConfig registryConfig = new RegistryConfig();
            registryConfig.setAddress(dubboMethodDto.getAddress());
            reference.setRegistry(registryConfig);
        }
        if (StringUtils.isNotBlank(dubboMethodDto.getVersion())) {
            reference.setVersion(dubboMethodDto.getVersion());
        }
        if (StringUtils.isNotBlank(dubboMethodDto.getGroup())) {
            reference.setGroup(dubboMethodDto.getGroup());
        }
        return reference;
    }

}