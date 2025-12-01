package cn.isqing.icloud.starter.admin.api.dto;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class MenuDto {
    private Long id;

    @NotEmpty(message = "菜单名称不能为空")
    private String name;

    private Long parentId;

    private String url;

    private String icon;

    private Integer sort;

    private Integer type; // 1:菜单 2:按钮

    private String permission;

    private Integer status;
}