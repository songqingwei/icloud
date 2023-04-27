package cn.isqing.icloud.starter.variable.api.dto;

import lombok.Data;

@Data
public class VariableListReq extends AuthDto {
    /**
     * 名称
     */
    private String name;

    private Long cid;
    private String busiCode;
    private Long actionId;

    private String type;
    private String createTimeConditionMin;
    private String createTimeConditionMax;
}
