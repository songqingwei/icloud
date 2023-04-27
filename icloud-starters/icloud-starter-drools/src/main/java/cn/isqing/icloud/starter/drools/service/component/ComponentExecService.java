package cn.isqing.icloud.starter.drools.service.component;

import cn.isqing.icloud.common.utils.dto.Response;
import cn.isqing.icloud.starter.drools.common.dto.ComponentExecDto;
import cn.isqing.icloud.starter.drools.dao.entity.Component;

/**
 * 组件执行服务
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface ComponentExecService {

    Response<Object> exec(Component component, ComponentExecDto dto);

}
