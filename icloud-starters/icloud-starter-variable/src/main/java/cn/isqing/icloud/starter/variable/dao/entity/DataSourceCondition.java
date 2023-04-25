package cn.isqing.icloud.starter.variable.dao.entity;

import cn.isqing.icloud.common.utils.dto.BaseCondition;
import lombok.Data;

@Data
public class DataSourceCondition extends BaseCondition {

    private Long id;

    private Integer isActive;

    private String nameConditionLike;

    private Integer type;

}
