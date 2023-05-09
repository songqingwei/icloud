package cn.isqing.icloud.starter.variable.api.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 授权信息
 */
@Data
public class ApiAuthDto {

    @NotNull
    private Integer domain;
    
    @NotEmpty
    private String domainAuthCode;
}
