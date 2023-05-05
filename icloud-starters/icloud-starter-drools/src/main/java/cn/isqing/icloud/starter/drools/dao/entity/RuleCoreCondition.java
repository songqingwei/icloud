package cn.isqing.icloud.starter.drools.dao.entity;

import cn.isqing.icloud.common.utils.dto.BaseCondition;
import lombok.Data;

@Data
public class RuleCoreCondition extends BaseCondition {

    // 域
    private Integer domain;

    // 动作
    private Long actionId;

    // 业务编码
    private String busiCode;


}
