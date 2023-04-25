package cn.isqing.icloud.starter.variable.service.variable.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class VariableListReq {
    /**
     * 名称
     */
    private String name;

    private Long cid;

    @NotBlank
    private Integer domain;

    private Long actionId;

    private String type;
    private String createTimeConditionMin;
    private String createTimeConditionMax;
}
