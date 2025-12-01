package cn.isqing.icloud.starter.admin.api.dto;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

@Data
public class RoleDto {
    private Long id;

    @NotEmpty(message = "角色名称不能为空")
    private String name;

    private String code;

    private String description;

    private Integer status;
}