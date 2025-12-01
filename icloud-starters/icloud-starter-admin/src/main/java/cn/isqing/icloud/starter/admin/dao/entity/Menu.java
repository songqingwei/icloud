package cn.isqing.icloud.starter.admin.dao.entity;

import lombok.Data;

@Data
public class Menu {
    private Long id = 0L;

    private String name;

    private Long parentId = 0L;

    private String url;

    private String icon;

    private Integer sort = 0;

    private Integer type; // 1:菜单 2:按钮

    private String permission;

    private Integer status = 1;
}