-- 用户表
CREATE TABLE `admin_user`
(
    `id`            bigint(20)                              NOT NULL DEFAULT '0' COMMENT '主键ID',
    `username`      varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '用户名',
    `password`      varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '密码',
    `nickname`      varchar(50) COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '昵称',
    `email`         varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '邮箱',
    `phone`         varchar(20) COLLATE utf8mb4_unicode_ci  NOT NULL DEFAULT '' COMMENT '手机号',
    `status`        int(1)                                  NOT NULL DEFAULT '1' COMMENT '状态 1:正常 2:禁用',
    `department_id` bigint(20)                              NOT NULL DEFAULT '0' COMMENT '部门ID',
    `create_time`   datetime                                         DEFAULT NULL COMMENT '创建时间',
    `update_time`   datetime                                         DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_department_id` (`department_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户表';

-- 部门表
CREATE TABLE `admin_department`
(
    `id`          bigint(20)                             NOT NULL DEFAULT '0' COMMENT '主键ID',
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '部门名称',
    `code`        varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '部门编码',
    `parent_id`   bigint(20)                             NOT NULL DEFAULT '0' COMMENT '父级ID',
    `description` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '描述',
    `sort`        int(11)                                NOT NULL DEFAULT '0' COMMENT '排序',
    `status`      int(1)                                 NOT NULL DEFAULT '1' COMMENT '状态 1:正常 2:禁用',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='部门表';

-- 角色表
CREATE TABLE `admin_role`
(
    `id`          bigint(20)                             NOT NULL DEFAULT '0' COMMENT '主键ID',
    `name`        varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '角色名称',
    `code`        varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '角色编码',
    `description` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '描述',
    `status`      int(1)                                 NOT NULL DEFAULT '1' COMMENT '状态 1:正常 2:禁用',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='角色表';

-- 菜单表
CREATE TABLE `admin_menu`
(
    `id`         bigint(20)                             NOT NULL DEFAULT '0' COMMENT '主键ID',
    `name`       varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '菜单名称',
    `parent_id`  bigint(20)                             NOT NULL DEFAULT '0' COMMENT '父级ID',
    `url`        varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '请求地址',
    `icon`       varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '图标',
    `sort`       int(11)                                NOT NULL DEFAULT '0' COMMENT '排序',
    `type`       int(1)                                 NOT NULL DEFAULT '1' COMMENT '类型 1:菜单 2:按钮',
    `permission` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '权限标识',
    `status`     int(1)                                 NOT NULL DEFAULT '1' COMMENT '状态 1:正常 2:禁用',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='菜单表';

-- 用户角色关系表
CREATE TABLE `admin_user_role`
(
    `id`      bigint(20) NOT NULL DEFAULT '0' COMMENT '主键ID',
    `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
    `role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户角色关系表';

-- 角色菜单关系表
CREATE TABLE `admin_role_menu`
(
    `id`      bigint(20) NOT NULL DEFAULT '0' COMMENT '主键ID',
    `role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
    `menu_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '菜单ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_menu_id` (`menu_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='角色菜单关系表';

-- 用户MFA信息表
CREATE TABLE `admin_user_mfa`
(
    `id`          bigint(20)                             NOT NULL DEFAULT '0' COMMENT '主键ID',
    `user_id`     bigint(20)                             NOT NULL DEFAULT '0' COMMENT '用户ID',
    `secret_key`  varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '密钥',
    `is_enabled`  int(1)                                 NOT NULL DEFAULT '0' COMMENT '是否启用 0:未启用 1:已启用',
    `create_time` datetime                               DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime                               DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户MFA信息表';