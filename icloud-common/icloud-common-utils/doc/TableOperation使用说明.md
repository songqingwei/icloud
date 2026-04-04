# 注解式快速增删改查工具使用说明

## 概述

本工具提供了基于注解的快速增删改查功能，通过简单的注解配置即可实现常见的数据库操作，并在操作完成后自动发布事件，方便其他模块订阅处理。

## 核心特性

1. **注解驱动**：使用 `@TableAction` 注解快速定义表操作
2. **自动扫描**：自动扫描并注册系统中的表信息
3. **事件机制**：增删改操作后自动发布 `TableChangeEvent` 事件
4. **灵活订阅**：支持多种方式订阅和处理表变更事件

## 快速开始

### 1. 基本使用

在 Controller 方法上添加 `@TableAction` 注解：

```java
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 分页查询用户
     */
    @TableAction(
        tableName = "sys_user", 
        action = ActionType.PAGE_QUERY, 
        voClass = UserVO.class
    )
    @PostMapping("/pageQuery")
    public Response<Object> pageQuery(@RequestBody PageReqDto<Object> request) {
        return null; // 实际逻辑由切面处理
    }

    /**
     * 新增用户
     */
    @TableAction(
        tableName = "sys_user", 
        action = ActionType.INSERT, 
        voClass = UserVO.class
    )
    @PostMapping("/insert")
    public Response<Object> insert(@RequestBody Object data) {
        return null; // 实际逻辑由切面处理
    }

    /**
     * 更新用户
     */
    @TableAction(
        tableName = "sys_user", 
        action = ActionType.UPDATE, 
        voClass = UserVO.class
    )
    @PostMapping("/update")
    public Response<Object> update(@RequestBody Object data) {
        return null; // 实际逻辑由切面处理
    }

    /**
     * 删除用户
     */
    @TableAction(
        tableName = "sys_user", 
        action = ActionType.DELETE, 
        voClass = UserVO.class
    )
    @PostMapping("/delete")
    public Response<Object> delete(@RequestBody Object data) {
        return null; // 实际逻辑由切面处理
    }
}
```

### 2. 注解参数说明

`@TableAction` 注解包含以下参数：

- **tableName**: 数据库表名（必填）
- **action**: 操作类型（必填），可选值：
  - `PAGE_QUERY`: 分页查询
  - `INSERT`: 新增
  - `UPDATE`: 更新
  - `DELETE`: 删除
  - `DETAIL`: 详情查询
  - `LIST_QUERY`: 列表查询
- **voClass**: 返回的VO类（可选）

### 3. 请求参数格式

#### 分页查询

```json
{
  "pageInfo": {
    "pageNum": 1,
    "pageSize": 10,
    "needTotal": true,
    "needList": true,
    "selectAll": false
  },
  "condition": {
    "username": "张三",
    "status": 1
  }
}
```

#### 新增/更新/删除

直接传入实体对象的 JSON 数据：

```json
{
  "id": 1,
  "username": "张三",
  "email": "zhangsan@example.com",
  "status": 1
}
```

## 事件订阅

本工具采用**三级路由机制**自动找到事件订阅者，通过 `@RouteType` 注解实现。

### 三级路由结构

```
TABLE_OP : INSERT/UPDATE/DELETE : 表名
  r1          r2                  r3
```

- **r1（第一级）**: `TABLE_OP` - 固定前缀，避免与用户自定义事件冲突
- **r2（第二级）**: 操作类型（INSERT/UPDATE/DELETE）
- **r3（第三级）**: 表名或业务标识

### 1. 订阅特定表的特定操作

```java
import cn.isqing.icloud.common.utils.annotation.RouteType;
import cn.isqing.icloud.common.utils.constants.TableOperationEventConstants;
import cn.isqing.icloud.common.utils.event.TableOperationEventMsg;
import cn.isqing.icloud.common.utils.event.TableOperationEventSubscriber;

@Component
@RouteType(
    r1 = TableOperationEventConstants.R1_TABLE_OPERATION,  // TABLE_OP
    r2 = TableOperationEventConstants.R2_INSERT,            // INSERT
    r3 = "sys_user"                                          // 表名
)
public class SysUserInsertSubscriber implements TableOperationEventSubscriber {

    @Override
    public void onEvent(TableOperationEventMsg eventMsg) {
        log.info("新用户创建: ID={}", eventMsg.getDataId());
        // 发送欢迎邮件
        // 初始化用户配置
        // 等等...
    }
}
```

### 2. 订阅所有表的插入操作

```java
@Component
@RouteType(
    r1 = TableOperationEventConstants.R1_TABLE_OPERATION,
    r2 = TableOperationEventConstants.R2_INSERT,
    r3 = ""  // 空表示匹配所有表
)
public class GlobalInsertSubscriber implements TableOperationEventSubscriber {

    @Override
    public void onEvent(TableOperationEventMsg eventMsg) {
        log.info("表 {} 新增数据: ID={}", 
            eventMsg.getTableName(), eventMsg.getDataId());
        // 处理所有表的新增操作
    }
}
```

### 3. 订阅多个操作类型

如果需要订阅同一个表的多个操作，可以创建多个订阅者：

```java
// 订阅用户表更新
@Component
@RouteType(
    r1 = TableOperationEventConstants.R1_TABLE_OPERATION,
    r2 = TableOperationEventConstants.R2_UPDATE,
    r3 = "sys_user"
)
public class SysUserUpdateSubscriber implements TableOperationEventSubscriber {
    @Override
    public void onEvent(TableOperationEventMsg eventMsg) {
        log.info("用户信息更新: ID={}", eventMsg.getDataId());
    }
}

// 订阅用户表删除
@Component
@RouteType(
    r1 = TableOperationEventConstants.R1_TABLE_OPERATION,
    r2 = TableOperationEventConstants.R2_DELETE,
    r3 = "sys_user"
)
public class SysUserDeleteSubscriber implements TableOperationEventSubscriber {
    @Override
    public void onEvent(TableOperationEventMsg eventMsg) {
        log.info("用户被删除: ID={}", eventMsg.getDataId());
    }
}
```

## 高级用法

### 1. 手动注册表信息

如果自动扫描无法满足需求，可以手动注册表信息：

```java
@Autowired
private TableInfoScanner tableInfoScanner;

public void registerTable() {
    TableInfoDto tableInfo = new TableInfoDto();
    tableInfo.setEntityClass(User.class);
    tableInfo.setMapper(userMapper);
    tableInfo.setConditionClass(UserCondition.class);
    
    tableInfoScanner.registerTableInfo("sys_user", tableInfo);
}
```

### 2. 结合业务逻辑

虽然注解可以快速实现增删改查，但你仍然可以在 Controller 方法中添加自定义逻辑：

```java
@TableAction(tableName = "sys_user", action = ActionType.INSERT)
@PostMapping("/insert")
public Response<Object> insert(@RequestBody Object data) {
    // 前置处理：数据校验、权限检查等
    validateData(data);
    
    // 实际插入操作由切面处理
    
    // 后置处理：发送通知等
    sendNotification();
    
    return null;
}
```

## 注意事项

1. **实体类命名规范**：实体类名应遵循驼峰命名，例如 `SysUser` 对应 Bean 名称 `sysUserMapper`
2. **Mapper 要求**：需要继承 `BaseMapper` 接口
3. **Condition 类**：分页查询需要提供对应的 Condition 类（可选）
4. **事务管理**：如需事务支持，请在 Service 层添加 `@Transactional` 注解
5. **事件异步处理**：默认事件是同步的，如需异步可配置 `@Async`

## 示例代码

参考 `TableOperationDemoController` 和 `TableChangeEventExampleListener` 查看完整示例。

## 常见问题

### Q: 如何自定义实体类扫描路径？

A: 修改 `TableInfoScanner` 中的 `basePackages` 数组，添加你的实体类包路径。

### Q: 事件处理失败会影响主流程吗？

A: 不会。事件处理异常已被捕获，不会影响增删改操作的结果。

### Q: 如何实现批量操作？

A: 当前版本暂不支持批量操作，建议在 Service 层自行实现。

## 技术架构

- **注解**: `@TableAction` - 标记需要自动处理的表操作方法
- **切面**: `TableOperationAspect` - 拦截注解方法，执行表操作
- **服务**: `TableOperationService` - 提供通用的增删改查实现
- **扫描器**: `TableInfoScanner` - 自动扫描并注册表信息
- **事件**: `TableChangeEvent` - 表变更事件，支持订阅处理
