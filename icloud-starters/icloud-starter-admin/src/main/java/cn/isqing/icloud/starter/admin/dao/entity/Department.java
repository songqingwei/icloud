package cn.isqing.icloud.starter.admin.dao.entity;

import lombok.Data;

@Data
public class Department {
    private Long id = 0L;

    private String name;

    private String code;

    private Long parentId = 0L;

    private String description;

    private Integer sort = 0;

    private Integer status = 1;
}