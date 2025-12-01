package cn.isqing.icloud.starter.admin.api.dto;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

@Data
public class UserLoginRequestDto {

    @NotEmpty(message = "用户名不能为空")
    private String username;

    @NotEmpty(message = "密码不能为空")
    private String password;
}