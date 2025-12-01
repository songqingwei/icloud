package cn.isqing.icloud.starter.admin.api.dto;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class MfaVerifyRequestDto {
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotEmpty(message = "验证码不能为空")
    private String code;
}