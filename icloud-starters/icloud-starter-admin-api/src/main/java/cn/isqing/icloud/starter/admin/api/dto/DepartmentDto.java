package cn.isqing.icloud.starter.admin.api.dto;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class DepartmentDto {
    private Long id;

    @NotEmpty(message = "部门名称不能为空")
    private String name;

    private String code;

    private Long parentId;

    private String description;

    private Integer sort;

    private Integer status;
}