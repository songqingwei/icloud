package cn.isqing.icloud.starter.variable.api.dto;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

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
