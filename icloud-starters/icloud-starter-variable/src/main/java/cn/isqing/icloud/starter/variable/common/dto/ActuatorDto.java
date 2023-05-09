package cn.isqing.icloud.starter.variable.common.dto;

import cn.isqing.icloud.starter.variable.api.dto.ApiVariableSimpleDto;
import cn.isqing.icloud.starter.variable.dao.entity.Component;
import lombok.Data;

import java.util.List;
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
    private List<List<Component>> componentList;
    // id:variable
    private Map<Long, ApiVariableSimpleDto> variableMap;

}
