# Admin Starter 模块测试指南

## 测试目标

验证icloud-starter-admin模块的基本功能，包括：
1. 模块是否能正确集成到Spring Boot应用中
2. Web管理界面是否能正常访问
3. 安全配置是否生效
4. 配置开关是否起作用

## 测试步骤

### 1. 准备工作

1. 确保icloud-starter-admin和icloud-starter-admin-api模块已经正确构建
2. 安装并启动MySQL数据库
3. 创建测试数据库并初始化表结构

### 2. 数据库准备

```sql
CREATE DATABASE IF NOT EXISTS icloud_admin DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

执行 `src/main/resources/db/init.sql` 文件中的SQL语句初始化表结构和基础数据。

### 3. 配置调整

编辑 `src/main/resources/application.yml` 文件：

1. 根据实际环境修改数据库连接信息：
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/icloud_admin?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
       username: root
       password: root
   ```

2. 确认admin模块启用配置：
   ```yaml
   i.admin:
     enabled: true
     web.pre: admin
     username: admin
     password: admin
   ```

### 4. 启动应用

通过IDE运行[cn.isqing.icloud.app.admin.Application](file://C:\Users\qing\IdeaProjects\icloud\icloud-apps\icloud-app-admin\src\main\java\cn\isqing\icloud\app\admin\Application.java)类的main方法启动应用。

或者使用Maven命令（如果环境中有Maven）：
```bash
cd icloud-apps/icloud-app-admin
mvn spring-boot:run
```

### 5. 功能测试

#### 5.1 管理界面访问测试

1. 打开浏览器访问 `http://localhost:8080/admin`
2. 应该弹出基础认证对话框
3. 输入用户名: admin, 密码: admin
4. 应该能看到管理界面

#### 5.2 安全配置测试

1. 输入错误的用户名或密码，应该被拒绝访问
2. 直接访问 `http://localhost:8080/admin/index.html` 应该也被要求认证

#### 5.3 配置开关测试

1. 修改 `application.yml` 中的配置：
   ```yaml
   i.admin:
     enabled: false
   ```
2. 重启应用
3. 访问 `http://localhost:8080/admin` 应该返回404错误

### 6. 预期结果

1. 当 `i.admin.enabled=true` 时，能够通过认证访问管理界面
2. 当 `i.admin.enabled=false` 时，管理界面路径不可访问
3. 认证机制正常工作，只有正确的用户名密码才能访问
4. 模块能正常加载，没有启动错误

## 故障排除

### 1. 启动失败

- 检查是否正确编译了admin相关模块
- 检查数据库连接配置是否正确
- 查看控制台输出的错误日志

### 2. 无法访问管理界面

- 检查配置项 `i.admin.enabled` 是否为true
- 检查网络连接和端口是否正确
- 检查是否有防火墙阻止访问

### 3. 认证失败

- 检查配置的用户名密码是否正确
- 检查数据库中是否存在初始化的用户数据
- 查看日志确认认证过程是否有错误