package cn.isqing.icloud.starter.variable.service.component.flow;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.common.utils.annotation.RouteType;
import cn.isqing.icloud.common.utils.json.JsonUtil;
import cn.isqing.icloud.starter.variable.common.constants.DataSourceTypeConstatnts;
import cn.isqing.icloud.starter.variable.common.enums.FunctionComponentDialectType;
import cn.isqing.icloud.starter.variable.service.component.ComponentRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.UnaryOperator;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
@Slf4j
@RouteType(r1 = DataSourceTypeConstatnts.FUNCTION)
public class FunctionComponentExecFlow extends BaseComponentExecFlow {

    @Autowired
    private ComponentRegister register;

    @Override
    protected void pre(ComponentExecContext context) {
        String config = context.getDialectConfig();
        String params = (String) JsonUtil.extract(config, FunctionComponentDialectType.PARAMS.getJsonPath());
        context.setRequestParamsTpl(new String[]{params});
    }

    @Override
    protected void execComponent(ComponentExecContext context) {
        String config = context.getDialectConfig();
        String name = (String) JsonUtil.extract(config, FunctionComponentDialectType.METHOD_NAME.getJsonPath());
        UnaryOperator<String> function = register.get(name);
        if (function == null) {
            log.error("function组件查询失败，请检查{}方法是否已实现", name);
            interrupt(context, Response.ERROR);
            return;
        }
        String res = null;
        try {
            res = function.apply(context.getRequestParamsTpl()[0]);
        } catch (Exception e) {
            log.error("function组件" + name + "方法执行异常", e);
            interrupt(context, Response.ERROR);
            return;
        }
        context.setExecRes(res);
    }


}
