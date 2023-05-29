package cn.isqing.icloud.starter.drools.dao.entity;

import cn.isqing.icloud.common.utils.dto.BaseCondition;
import lombok.Data;

import java.util.List;

@Data
public class RuleTemplateCondition extends BaseCondition {

    private Long idConditionMin;

    private List<Long> idList;
    /**
     * 规则名称
     */
    private String name;

    private Long orgId;

    private Integer domain;

    private String createTimeConditionMin;

    private String createTimeConditionMax;

    private Integer isDel;

    private Integer isActive;

}
