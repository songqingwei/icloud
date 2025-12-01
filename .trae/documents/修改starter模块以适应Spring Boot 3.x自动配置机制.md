## 问题分析
当前项目使用的是Spring Boot 3.x版本，但starter模块仍然使用旧的spring.factories机制来配置自动配置类。根据博客文章，Spring Boot 3.x已经移除了spring.factories机制，改用新的自动配置导入方式。

## 解决方案
为每个starter模块创建新的自动配置导入文件，以适应Spring Boot 3.x的自动配置机制，同时保留原有的spring.factories文件以兼容旧版本。

## 实施步骤

### 1. 修改icloud-starter-admin模块
- 创建目录：`src/main/resources/META-INF/spring`
- 创建文件：`org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- 写入内容：`cn.isqing.icloud.starter.admin.AutoConfiguration`

### 2. 修改icloud-starter-drools模块
- 创建目录：`src/main/resources/META-INF/spring`
- 创建文件：`org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- 写入内容：`cn.isqing.icloud.starter.drools.AutoConfiguration`

### 3. 修改icloud-starter-variable模块
- 创建目录：`src/main/resources/META-INF/spring`
- 创建文件：`org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- 写入内容：`cn.isqing.icloud.starter.variable.AutoConfiguration`

### 4. 保留原有spring.factories文件
- 不删除现有的spring.factories文件，以兼容旧版本Spring Boot
- 新的自动配置导入文件将被Spring Boot 3.x优先使用

## 预期结果
- 所有starter模块都支持Spring Boot 3.x的自动配置机制
- 同时保持对旧版本Spring Boot的兼容性
- 自动配置类能够被正确扫描和加载

## 验证要点
- 每个starter模块都有新的自动配置导入文件
- 导入文件中包含正确的自动配置类全类名
- 原有spring.factories文件仍然存在
- 应用能够正常启动，自动配置被正确触发