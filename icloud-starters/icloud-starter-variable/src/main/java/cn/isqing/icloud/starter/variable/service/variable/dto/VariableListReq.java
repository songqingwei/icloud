package cn.isqing.icloud.starter.variable.service.variable.dto;

import cn.isqing.icloud.starter.variable.api.dto.AuthDto;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class VariableListReq extends AuthDto {
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
