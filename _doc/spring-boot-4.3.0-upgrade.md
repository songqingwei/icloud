# Spring Boot 4.3.0 升级计划

## 背景

将 iCloud 项目从 Spring Boot **3.5.8** 升级到 **4.3.0**。

> **注意**：Spring Boot 4.x 基于 Spring Framework 7.x，要求 Java **21+**（原项目为 Java 17，已同步升级至 21）。

---

## 升级涉及文件清单

| 文件 | 变更类型 |
|------|----------|
| `pom.xml`（根） | 依赖版本升级、MySQL Connector 重命名 |
| `icloud-common/pom.xml` | Java 编译版本 17→21 |
| `icloud-common/icloud-common-utils/pom.xml` | Java 编译版本 17→21 |
| `icloud-common/icloud-common-api/pom.xml` | Java 编译版本 17→21 |
| `icloud-common-utils/.../AutoConfiguration.java` | `StringUtils.isEmpty` → `StringUtils.hasText`，注释更新 |

---

## 依赖版本对照表

| 依赖 | 旧版本 | 新版本 | 说明 |
|------|--------|--------|------|
| `spring-boot.version` | 3.5.8 | **4.3.0** | 核心升级目标 |
| `java.version` / `maven.compiler.*` | 17 | **21** | Spring Boot 4.x 最低要求 Java 21 |
| `revision`（项目版本） | 4.0.3.0-SNAPSHOT | **4.3.0-SNAPSHOT** | 跟随版本 |
| `maven-compiler-plugin.version` | 3.8.1 | **3.13.0** | 支持 Java 21 |
| `dubbo.version` | 3.2.5 | **3.3.2** | 适配 Spring Framework 7.x |
| `dubbo-spring-boot.version` | 3.2.0 | **3.3.2** | 同步 Dubbo |
| `fastjson2.version` | 2.0.9 | **2.0.57** | 升级至最新稳定版 |
| `gson.version` | 2.9.0 | **2.11.0** | 最新版 |
| `commons-lang3.version` | 3.12.0 | **3.17.0** | 最新版 |
| `mybatis.version` | 3.5.11 | **3.5.19** | 最新版 |
| `hutool.version` | 5.8.15 | **5.8.38** | 最新版 |
| `rocketmq-spring-boot.version` | 2.2.3 | **2.3.1** | 最新版 |
| `druid-spring-boot.version` | 1.2.23 | **1.2.24** | 最新版 |
| `mybatis-spring-boot.version` | 3.0.3 | **3.0.4** | 兼容 Spring Boot 4.x |
| `lombok.version` | 1.18.26 | **1.18.36** | 支持 Java 21 |
| `mysql-connector`（groupId/artifactId） | `mysql:mysql-connector-java:8.0.33` | `com.mysql:mysql-connector-j:9.2.0` | **重命名**（SB 3.1+ 官方已重命名） |
| `redisson-spring-boot.version` | 3.39.0 | 3.39.0 | 已兼容，无需修改 |
| `xxl-job.version` | 2.4.2 | 2.4.2 | 暂无新版本，保持 |
| `curator-framework.version` | 5.7.1 | 5.7.1 | 保持 |
| `zookeeper.version` | 3.9.3 | 3.9.3 | 保持 |

---

## Spring Boot 4.x 关键变更说明

### 1. Java 21 基线
- Spring Boot 4.x 基于 Spring Framework 7.x，**最低 Java 21**
- 项目所有模块 `maven.compiler.source/target` 已从 17 升至 21

### 2. Jakarta EE 版本
- Spring Boot 3.x → Jakarta EE 10
- Spring Boot 4.x → Jakarta EE **11**（API 包名不变，仍为 `jakarta.*`，无需代码改动）
- 本项目在 Spring Boot 3.x 升级时已完成 `javax.*` → `jakarta.*` 迁移，**无残留 `javax.*` 导入**，直接兼容

### 3. Spring Framework API 移除
- `org.springframework.util.StringUtils.isEmpty()` 在 Spring Framework 7.x 中**已彻底移除**
- 已修改 `AutoConfiguration.java`：`!StringUtils.isEmpty(x)` → `StringUtils.hasText(x)`
- 其他文件中出现的 `StringUtils.isEmpty()` 均来自 `org.apache.commons.lang3.StringUtils`（不受影响）

### 4. spring.factories 自动配置机制
- 已于 Spring Boot 3.0 废弃 `spring.factories`，改用 `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- 本项目已使用新机制，Spring Boot 4.x 继续支持，**无需修改**

### 5. MySQL Connector 重命名
- Maven 坐标变更：`mysql:mysql-connector-java` → `com.mysql:mysql-connector-j`
- 版本从 8.0.33 升级至 9.2.0（支持 MySQL 8.x / 9.x）

---

## 执行计划与进度

| # | 任务 | 状态 | 完成时间 |
|---|------|------|----------|
| 1 | 分析项目结构与升级影响范围 | ✅ 完成 | 2026-04-05 |
| 2 | 创建升级计划文档（本文档） | ✅ 完成 | 2026-04-05 |
| 3 | 升级根 `pom.xml` 依赖版本 | ✅ 完成 | 2026-04-05 |
| 4 | 升级子模块 Java 编译版本（17→21） | ✅ 完成 | 2026-04-05 |
| 5 | 修复 `AutoConfiguration.java`（`StringUtils.isEmpty` 废弃 API） | ✅ 完成 | 2026-04-05 |
| 6 | 确认无 `javax.*` 残留（Jakarta EE 兼容性） | ✅ 完成 | 2026-04-05 |
| 7 | 确认无 `spring.factories`（自动配置机制兼容性） | ✅ 完成 | 2026-04-05 |
| 8 | 更新进度文档 | ✅ 完成 | 2026-04-05 |

**整体状态：所有变更已完成 ✅**

---

## 执行日志

### 2026-04-05

**分析阶段**
- 项目结构：多模块 Maven，`icloud` → `icloud-common` → `icloud-common-api` + `icloud-common-utils`
- 当前版本：Spring Boot 3.5.8，Java 17
- Jakarta EE 导入：已全部迁移（无 `javax.*` 残留）
- 自动配置机制：已使用新版 `.imports` 文件
- 发现问题：
  1. `AutoConfiguration.java` 使用 `StringUtils.isEmpty()`（Spring Framework 7 已移除）
  2. MySQL Connector GroupId/ArtifactId 使用旧版命名
  3. Java 版本需从 17 升至 21

**修改阶段**
- ✅ `pom.xml`：全面升级所有依赖版本，MySQL Connector 重命名
- ✅ `icloud-common/pom.xml`：Java 编译版本 17→21
- ✅ `icloud-common-utils/pom.xml`：Java 编译版本 17→21
- ✅ `icloud-common-api/pom.xml`：Java 编译版本 17→21
- ✅ `AutoConfiguration.java`：`!StringUtils.isEmpty(x)` → `StringUtils.hasText(x)`，注释更新

### 2026-04-05（构建修复）

**问题**：执行 `mvn clean` 时报错：
```
'dependencies.dependency.version' for org.springframework.boot:spring-boot-starter-aop:jar is missing
```

**根本原因**：Spring Boot 4.x 正式版首发于 `repo.spring.io/release`，阿里云镜像（`maven.aliyun.com`）对 Spring Boot 4.x 存在同步延迟，Maven 无法解析 `spring-boot-dependencies` BOM，导致所有 BOM 管理的 starter 版本均缺失。

**修复**：在 `pom.xml` 的 `<repositories>` 和 `<pluginRepositories>` 中首位加入 `repo.spring.io/release` 仓库，确保 Spring Boot 4.x BOM 及制品可被正常下载。

- ✅ `pom.xml`：repositories + pluginRepositories 增加 `spring-releases`（`https://repo.spring.io/release`）

---

## 升级后注意事项

1. **本地 JDK**：确保开发环境和 CI 使用 JDK 21+
2. **Spring Boot 4.x 仓库**：`repo.spring.io/release` 是 Spring Boot 4.x 的首发仓库，已加入 pom.xml；阿里云镜像同步后两者均可用
3. **Dubbo 兼容性**：Dubbo 3.3.x 对 Spring Framework 7 提供官方支持，升级后需完整测试 RPC 调用
4. **Druid 兼容性**：`druid-spring-boot-starter` 1.2.x 对 Spring Boot 4.x 的官方支持有待确认，如编译失败可考虑使用 `druid-spring-boot-3-starter`
5. **Hutool 5.x**：Hutool 5.x 基于 `javax.*` 命名空间，部分功能在 Jakarta EE 11 环境下需谨慎使用，可关注 Hutool 6.x 发布进度
