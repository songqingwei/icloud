package cn.isqing.icloud.starter.variable.service.component;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.variable.common.dto.ComponentExecDto;
import cn.isqing.icloud.starter.variable.dao.entity.Component;

/**
 * 组件执行服务
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface ComponentExecService {

    Response<Object> exec(Component component, ComponentExecDto dto);

}
