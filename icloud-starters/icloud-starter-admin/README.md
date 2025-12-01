# Admin管理模块

## 功能介绍

Admin管理模块提供了基于Spring Security的用户、角色、权限管理系统，包含以下功能：

1. 用户管理
2. 部门管理
3. 角色管理
4. 菜单权限管理
5. 基于Web的管理界面

## 快速开始

### 1. 添加依赖

在您的项目中添加以下依赖：

```xml
<dependency>
    <groupId>cn.isqing.icloud</groupId>
    <artifactId>icloud-starter-admin</artifactId>
    <version>${project.version}</version>
</dependency>
```

### 2. 数据库初始化

执行以下SQL脚本初始化数据库表：

```sql
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
```

### 3. 配置项

在`application.yml`中添加以下配置：

```yaml
# Admin模块配置
i.admin:
  # 是否启用Admin模块
  enabled: true
  # 是否启用Admin Web页面访问
  web.enabled: true
  # Web访问前缀
  web.pre: iadmin
  # 登录用户名
  username: admin
  # 登录密码（当数据库用户密码为空时使用）
  password: admin
```

### 4. 访问管理界面

启动应用后，访问以下地址进入管理界面：

```
http://localhost:8080/iadmin
```

系统提供两种方式访问管理界面：
1. 表单登录：访问首页会重定向到登录页面 `/admin/login`
2. 直接访问受保护资源会跳转到登录页面

默认用户名：`admin`
默认密码：`admin`（当数据库用户密码为空时使用）

## 功能说明

### 用户管理
- 用户增删改查
- 用户角色分配

### 部门管理
- 部门树形结构管理
- 部门信息维护

### 角色管理
- 角色增删改查
- 角色权限分配

### 菜单管理
- 菜单树形结构管理
- 权限标识配置

## 安全说明

1. 所有密码都使用BCrypt加密存储
2. 使用Spring Security进行权限控制
3. 用户认证信息从数据库读取
4. 当数据库用户密码为空时，使用配置的默认密码进行认证
5. admin用户拥有所有权限
6. admin用户不允许被删除或修改
7. 可通过配置项启用或禁用Admin模块
8. 可通过配置项独立控制Web页面访问

## 扩展开发

1. 实现自定义的UserDetailsService进行用户认证
2. 扩展权限控制逻辑
3. 自定义管理界面