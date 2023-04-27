package cn.isqing.icloud.starter.variable.dao.entity;

import cn.isqing.icloud.common.utils.dto.BaseCondition;
import lombok.Data;

import java.util.List;

@Data
public class VariableCondition extends BaseCondition {
    /**
     * 名称
     */
    private String name;
    private String busiCode;
    private Long cid;
    private List<Long> cidCondition;
    private Integer domain;
    private String type;
    private String createTimeConditionMin;
    private String createTimeConditionMax;
    private Integer isDel;
}
