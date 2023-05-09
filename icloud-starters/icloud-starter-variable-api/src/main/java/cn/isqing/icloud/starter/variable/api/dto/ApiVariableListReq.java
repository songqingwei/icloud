package cn.isqing.icloud.starter.variable.api.dto;

import lombok.Data;

import java.util.List;

@Data
public class ApiVariableListReq extends ApiAuthDto {
    /**
     * 名称
     */
    private String name;

    private Long cid;
    private List<Long> idCondition;
    private String busiCode;
    private Long actionId;

    private String type;
    private String createTimeConditionMin;
    private String createTimeConditionMax;
}
