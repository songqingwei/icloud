# 自动API路由使用指南（零配置）

## 🚀 概述

通过 URL 路径自动推断表名和操作类型，**完全零配置**即可实现 CRUD 功能。

### ✨ 核心特性

- ✅ **零配置**：无需任何配置文件
- ✅ **智能推断**：从 URL 自动解析表名和操作
- ✅ **完整Spring管理**：拦截器、Filter、日志全部生效
- ✅ **支持6种操作**：page/insert/update/delete/detail/list

---

## 📋 URL 规范

```
POST /auto-api/{tableName}/{action}
```

### 支持的操作类型

| 操作 | URL示例 | 说明 |
|------|---------|------|
| page | `/auto-api/sys_user/page` | 分页查询 |
| insert | `/auto-api/sys_user/insert` | 插入数据 |
| update | `/auto-api/sys_user/update` | 更新数据 |
| delete | `/auto-api/sys_user/delete` | 删除数据 |
| detail | `/auto-api/sys_user/detail` | 查询详情（根据ID） |
| list | `/auto-api/sys_user/list` | 列表查询（不分页） |

---

## 💡 使用示例

### 前提条件

确保以下类存在且遵循命名规范：

```
cn.isqing.icloud.dal.entity.SysUser          # Entity类
cn.isqing.icloud.dal.condition.SysUserCondition  # Condition类
cn.isqing.icloud.dal.mapper.SysUserMapper    # Mapper接口
```

TableInfoScanner 会自动扫描并注册这些类。

---

### 示例 1：分页查询

**请求：**
```bash
POST http://localhost:8080/auto-api/sys_user/page
Content-Type: application/json

{
  "pageInfo": {
    "pageNum": 1,
    "pageSize": 20,
    "needTotal": true,
    "needList": true
  },
  "condition": {
    "username": "张三",
    "orderBy": "create_time DESC"
  }
}
```

**响应：**
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "total": 100,
    "list": [
      {
        "id": 1,
        "username": "张三",
        "email": "zhangsan@example.com",
        "createTime": "2024-01-01 10:00:00"
      }
    ]
  }
}
```

---

### 示例 2：插入数据

**请求：**
```bash
POST http://localhost:8080/auto-api/sys_user/insert
Content-Type: application/json

{
  "username": "李四",
  "email": "lisi@example.com",
  "age": 25,
  "status": 1
}
```

**响应：**
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

---

### 示例 3：更新数据

**请求：**
```bash
POST http://localhost:8080/auto-api/sys_user/update
Content-Type: application/json

{
  "id": 1,
  "username": "张三三",
  "email": "zhangsan_new@example.com"
}
```

**响应：**
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

---

### 示例 4：删除数据

**请求：**
```bash
POST http://localhost:8080/auto-api/sys_user/delete
Content-Type: application/json

{
  "id": 1
}
```

**响应：**
```json
{
  "code": 200,
  "msg": "success",
  "data": null
}
```

---

### 示例 5：查询详情

**请求：**
```bash
POST http://localhost:8080/auto-api/sys_user/detail
Content-Type: application/json

{
  "id": 1
}
```

**响应：**
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "id": 1,
    "username": "张三",
    "email": "zhangsan@example.com",
    "age": 25,
    "status": 1,
    "createTime": "2024-01-01 10:00:00"
  }
}
```

---

### 示例 6：列表查询（不分页）

**请求：**
```bash
POST http://localhost:8080/auto-api/sys_user/list
Content-Type: application/json

{
  "status": 1,
  "orderBy": "create_time DESC"
}
```

**响应：**
```json
{
  "code": 200,
  "msg": "success",
  "data": [
    {
      "id": 1,
      "username": "张三",
      "email": "zhangsan@example.com"
    },
    {
      "id": 2,
      "username": "李四",
      "email": "lisi@example.com"
    }
  ]
}
```

---

## 🔧 工作原理

### 请求处理流程

```
客户端请求: POST /auto-api/sys_user/page
    ↓
Spring Boot Filter Chain（日志、认证等）
    ↓
DispatcherServlet
    ↓
AutoApiRouteInterceptor（匹配 /auto-api/**）
    ↓
解析URL路径
    ├─ tableName = "sys_user"
    └─ action = "page"
    ↓
验证表是否存在（通过 TableInfoScanner）
    ↓
读取请求体 JSON
    ↓
构建 TableOperationDto
    ↓
调用 TableOperationService.execute()
    ↓
执行对应的数据库操作
    ↓
发布事件（INSERT/UPDATE/DELETE 时）
    ↓
返回结果
```

### 智能推断逻辑

1. **从 URL 提取表名**
   ```
   /auto-api/sys_user/page → tableName = "sys_user"
   ```

2. **从 URL 提取操作类型**
   ```
   page → ActionType.PAGE_QUERY
   insert → ActionType.INSERT
   update → ActionType.UPDATE
   delete → ActionType.DELETE
   detail → ActionType.DETAIL
   list → ActionType.LIST_QUERY
   ```

3. **从 TableInfoScanner 获取表信息**
   - Entity 类
   - Condition 类
   - Mapper 实例

4. **自动映射 JSON 到对象**
   - 根据 Entity/Condition 类自动反序列化

---

## 🎯 高级用法

### 事件订阅

在增删改操作后，会自动发布事件：

```java
@Component
@RouteType(
    r1 = TableOperationEventConstants.R1_TABLE_OPERATION,
    r2 = TableOperationEventConstants.R2_INSERT,
    r3 = "sys_user"
)
public class UserInsertSubscriber implements TableOperationEventSubscriber {
    
    @Override
    public void onEvent(TableOperationEventMsg eventMsg) {
        log.info("新用户创建，ID: {}", eventMsg.getDataId());
        // 发送通知、记录审计日志等
    }
}
```

### 全局事件订阅

```java
@Component
@RouteType(
    r1 = TableOperationEventConstants.R1_TABLE_OPERATION,
    r2 = TableOperationEventConstants.R2_INSERT
    // 不指定 r3，订阅所有表的插入事件
)
public class GlobalInsertSubscriber implements TableOperationEventSubscriber {
    
    @Override
    public void onEvent(TableOperationEventMsg eventMsg) {
        log.info("表 {} 插入数据，ID: {}", 
            eventMsg.getTableName(), 
            eventMsg.getDataId());
    }
}
```

---

## ⚠️ 注意事项

### 1. 命名规范

必须遵循以下包路径和命名规范：

```
xxx.mapper.XxxMapper      # Mapper接口
xxx.entity.Xxx            # Entity类
xxx.condition.XxxCondition # Condition类
```

例如：
```
cn.isqing.icloud.dal.mapper.SysUserMapper
cn.isqing.icloud.dal.entity.SysUser
cn.isqing.icloud.dal.condition.SysUserCondition
```

### 2. 表名格式

URL 中的表名应该是数据库中的实际表名（通常是下划线分隔）：
- ✅ `/auto-api/sys_user/page`
- ❌ `/auto-api/SysUser/page`

### 3. 只支持 POST 请求

所有操作都使用 POST 方法，参数通过 JSON body 传递。

### 4. 错误处理

如果表不存在或操作失败，会返回统一的错误响应：

```json
{
  "code": 500,
  "msg": "未找到表: invalid_table",
  "data": null
}
```

---

## 🔍 调试技巧

### 查看已注册的表

启动时会输出日志：
```
注册表信息: sys_user -> Entity: SysUser, Condition: SysUserCondition
注册表信息: t_order -> Entity: Order, Condition: OrderCondition
...
```

### 开启详细日志

```yaml
logging:
  level:
    cn.isqing.icloud.common.utils: DEBUG
```

### 测试 API

```bash
# 测试分页查询
curl -X POST http://localhost:8080/auto-api/sys_user/page \
  -H "Content-Type: application/json" \
  -d '{"pageInfo":{"pageNum":1,"pageSize":10},"condition":{}}'

# 测试插入
curl -X POST http://localhost:8080/auto-api/sys_user/insert \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@example.com"}'
```

---

## 📊 对比其他方式

| 特性 | 自动API | 配置式路由 | 注解式 |
|------|---------|-----------|--------|
| 配置量 | 零配置 | 需配置YAML | 需写Controller |
| 灵活性 | 高 | 中 | 低 |
| 学习成本 | 极低 | 低 | 中 |
| 适用场景 | 标准CRUD | 需要自定义 | 复杂业务 |
| URL规范性 | 统一规范 | 可自定义 | 完全自定义 |

---

## 🎉 总结

自动API路由让你：
- ✅ **零配置**：无需任何配置文件
- ✅ **快速开发**：只需定义 Entity/Condition/Mapper
- ✅ **统一规范**：所有 CRUD 接口遵循相同模式
- ✅ **完整集成**：享受 Spring 的所有特性

开始使用吧！🚀
