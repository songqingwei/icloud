package cn.isqing.icloud.starter.admin.dao.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserMfa {
    private Long id = 0L;

    private Long userId;

    private String secretKey;

    private Integer isEnabled = 0;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}