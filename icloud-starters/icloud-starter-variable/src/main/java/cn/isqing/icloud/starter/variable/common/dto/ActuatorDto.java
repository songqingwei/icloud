package cn.isqing.icloud.starter.variable.common.dto;

import cn.isqing.icloud.starter.variable.api.dto.VariableSimpleDto;
import cn.isqing.icloud.starter.variable.dao.entity.Component;
import lombok.Data;

import java.util.Deque;
import java.util.Map;

/**
 * 执行器dto对象
 *
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
@Data
public class ActuatorDto {

    // 组件拓扑图
    private Deque<Component> componentDeque;
    // id:variable
    private Map<Long, VariableSimpleDto> variableMap;

}
