package cn.isqing.icloud.starter.variable.dao.entity;

import cn.isqing.icloud.common.utils.dto.BaseCondition;
import lombok.Data;

@Data
public class ActionVariableCondition extends BaseCondition {

    private Long id;

    private Long actionId;

    // 变量id
    private Long vid;

}
