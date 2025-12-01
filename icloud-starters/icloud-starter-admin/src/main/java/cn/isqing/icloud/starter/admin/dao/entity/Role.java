package cn.isqing.icloud.starter.admin.dao.entity;

import lombok.Data;

@Data
public class Role {
    private Long id = 0L;

    private String name;

    private String code;

    private String description;

    private Integer status = 1;
}