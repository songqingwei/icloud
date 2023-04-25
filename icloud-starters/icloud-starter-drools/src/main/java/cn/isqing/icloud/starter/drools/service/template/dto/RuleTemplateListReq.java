package cn.isqing.icloud.starter.drools.service.template.dto;

import lombok.Data;

import java.util.List;

@Data
public class RuleTemplateListReq {
    /**
     * 规则名称
     */
    private String name;

    private Long orgId;

    private Integer domain;

    // 关联业务
    private List<String> busiCode;

    private String createTimeConditionMin;
    private String createTimeConditionMax;

}
