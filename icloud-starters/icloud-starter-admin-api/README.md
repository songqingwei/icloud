# Admin API 模块

## 功能介绍

Admin API模块提供了Admin管理系统的接口定义，包含用户、部门、角色、菜单和权限管理相关的API接口和数据传输对象。

## 主要组件

### 接口定义
- `AdminInterface`: Admin管理核心接口

### 数据传输对象
- `UserDto`: 用户信息
- `DepartmentDto`: 部门信息
- `RoleDto`: 角色信息
- `MenuDto`: 菜单信息
- `UserLoginRequestDto`: 用户登录请求

## 使用说明

此模块主要提供接口定义和数据传输对象，具体实现在`icloud-starter-admin`模块中。