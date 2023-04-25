package cn.isqing.icloud.starter.variable.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
public class VariablesValueReqDto extends AuthDto {

    @NotNull
    private Long coreId;

    @NotNull
    private Integer domain;

    private String inputParams;

    /**
     * 上下文组建运行结果
     * 组件id，json
     */
    private Map<Long, String> aboveResMap;

}
