# Admin Starter 测试应用

## 简介

这是一个用于测试icloud-starter-admin模块的简单Spring Boot应用程序。

## 如何运行

1. 确保已经正确编译了icloud-starter-admin和icloud-starter-admin-api模块

2. 创建MySQL数据库并执行初始化脚本:
   ```sql
   CREATE DATABASE IF NOT EXISTS icloud_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
   然后执行 `src/main/resources/db/init.sql` 文件中的SQL语句

3. 修改 `src/main/resources/application.yml` 中的数据库连接配置:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/icloud_admin?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
       username: root
       password: root
   ```

4. 运行Application类的main方法启动应用

## 访问管理界面

应用启动后，可以通过以下地址访问管理界面:

```
http://localhost:8080/admin
```

默认登录账号密码:
- 用户名: admin
- 密码: admin

## 配置说明

应用的主要配置在 `application.yml` 文件中:

```yaml
# admin starter 配置
i.admin:
  # 启用admin模块
  enabled: true
  # web访问前缀
  web.pre: admin
  # 登录用户名
  username: admin
  # 登录密码
  password: admin
```

## 功能验证点

1. 访问/admin路径应该会弹出登录框
2. 输入正确的用户名密码可以进入管理界面
3. 输入错误的用户名密码会被拒绝访问
4. 当i.admin.enabled=false时，/admin路径应该无法访问