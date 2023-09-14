package cn.isqing.icloud.starter.variable.service.component.flow;

import cn.isqing.icloud.common.utils.annotation.RouteType;
import cn.isqing.icloud.starter.variable.common.constants.DataSourceTypeConstatnts;
import cn.isqing.icloud.starter.variable.common.dto.ComponentExecDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Service
@Slf4j
@RouteType(r1 = DataSourceTypeConstatnts.DEFAULT)
public class DefaultComponentExecFlow extends BaseComponentExecFlow {

    @Override
    protected void registerRes(ComponentExecContext context) {
        ComponentExecDto resDto = context.getExecDto();
        resDto.getAboveResMap().put(context.getComponent().getId(), context.getExecRes());
    }

    @Override
    protected void execComponent(ComponentExecContext context) {
        context.setExecRes(context.getDialectConfig());
    }


}
