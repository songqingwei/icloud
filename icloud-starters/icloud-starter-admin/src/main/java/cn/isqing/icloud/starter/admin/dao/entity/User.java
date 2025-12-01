package cn.isqing.icloud.starter.admin.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id = 0L;

    private String username;

    private String password;

    private String nickname;

    private String email;

    private String phone;

    private Integer status = 1;

    private Long departmentId;

    // 业务日期字段允许为空
    private LocalDateTime createTime;

    // 业务日期字段允许为空
    private LocalDateTime updateTime;
}