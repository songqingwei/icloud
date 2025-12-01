package cn.isqing.icloud.starter.admin.api.dto;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;

    @NotEmpty(message = "用户名不能为空")
    private String username;

    private String password;

    @NotEmpty(message = "昵称不能为空")
    private String nickname;

    private String email;

    private String phone;

    private Integer status;

    private Long departmentId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}