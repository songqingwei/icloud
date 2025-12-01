package cn.isqing.icloud.starter.admin.dao.entity;

import lombok.Data;

@Data
public class RoleMenu {
    private Long id = 0L;

    private Long roleId;

    private Long menuId;
}