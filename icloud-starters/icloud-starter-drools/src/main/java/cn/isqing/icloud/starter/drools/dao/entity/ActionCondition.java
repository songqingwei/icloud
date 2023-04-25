package cn.isqing.icloud.starter.drools.dao.entity;

import cn.isqing.icloud.common.utils.dto.BaseCondition;
import lombok.Data;

@Data
public class ActionCondition extends BaseCondition {

    private Long id;

    private Integer isActive;

    private String nameConditionLike;

    private Long cid;

}
