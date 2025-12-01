package cn.isqing.icloud.starter.admin.dao.entity;

import lombok.Data;

@Data
public class UserRole {
    private Long id = 0L;

    private Long userId;

    private Long roleId;
}