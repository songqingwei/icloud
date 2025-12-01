package cn.isqing.icloud.starter.admin.api;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.admin.api.dto.*;

import java.util.List;
import java.util.Set;

/**
 * @author songqingwei@aliyun.com
 * @version 1.0
 **/
public interface AdminInterface {

    /**
     * 用户登录
     *
     * @param reqDto 登录请求参数
     * @return 登录结果
     */
    Response<String> login(UserLoginRequestDto reqDto);

    /**
     * 用户注册
     *
     * @param userDto 用户信息
     * @return 注册结果
     */
    Response<Boolean> register(UserDto userDto);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    Response<UserDto> getUserById(Long userId);

    /**
     * 更新用户信息
     *
     * @param userDto 用户信息
     * @return 更新结果
     */
    Response<Boolean> updateUser(UserDto userDto);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     * @return 删除结果
     */
    Response<Boolean> deleteUser(Long userId);

    /**
     * 分页查询用户列表
     *
     * @param req 请求参数
     * @return 用户列表
     */
    Response<PageResDto<UserDto>> listUsers(PageReqDto<UserDto> req);

    /**
     * 获取用户角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    Response<List<RoleDto>> getUserRoles(Long userId);

    /**
     * 分配用户角色
     *
     * @param userId  用户ID
     * @param roleIds 角色ID列表
     * @return 分配结果
     */
    Response<Boolean> assignUserRole(Long userId, Set<Long> roleIds);

    /**
     * 获取所有部门列表
     *
     * @return 部门列表
     */
    Response<List<DepartmentDto>> getAllDepartments();

    /**
     * 添加部门
     *
     * @param departmentDto 部门信息
     * @return 添加结果
     */
    Response<Boolean> addDepartment(DepartmentDto departmentDto);

    /**
     * 更新部门信息
     *
     * @param departmentDto 部门信息
     * @return 更新结果
     */
    Response<Boolean> updateDepartment(DepartmentDto departmentDto);

    /**
     * 删除部门
     *
     * @param departmentId 部门ID
     * @return 删除结果
     */
    Response<Boolean> deleteDepartment(Long departmentId);

    /**
     * 获取所有角色列表
     *
     * @return 角色列表
     */
    Response<List<RoleDto>> getAllRoles();

    /**
     * 添加角色
     *
     * @param roleDto 角色信息
     * @return 添加结果
     */
    Response<Boolean> addRole(RoleDto roleDto);

    /**
     * 更新角色信息
     *
     * @param roleDto 角色信息
     * @return 更新结果
     */
    Response<Boolean> updateRole(RoleDto roleDto);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return 删除结果
     */
    Response<Boolean> deleteRole(Long roleId);

    /**
     * 获取角色菜单权限
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    Response<List<MenuDto>> getRoleMenus(Long roleId);

    /**
     * 分配角色菜单权限
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID列表
     * @return 分配结果
     */
    Response<Boolean> assignRoleMenu(Long roleId, Set<Long> menuIds);

    /**
     * 获取所有菜单列表
     *
     * @return 菜单列表
     */
    Response<List<MenuDto>> getAllMenus();

    /**
     * 添加菜单
     *
     * @param menuDto 菜单信息
     * @return 添加结果
     */
    Response<Boolean> addMenu(MenuDto menuDto);

    /**
     * 更新菜单信息
     *
     * @param menuDto 菜单信息
     * @return 更新结果
     */
    Response<Boolean> updateMenu(MenuDto menuDto);

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     * @return 删除结果
     */
    Response<Boolean> deleteMenu(Long menuId);
    
    /**
     * 生成MFA二维码
     *
     * @param userId 用户ID
     * @return 二维码图片URL
     */
    Response<String> generateMfaQRCode(Long userId);

    /**
     * 启用MFA
     *
     * @param reqDto MFA启用请求参数
     * @return 启用结果
     */
    Response<Boolean> enableMfa(MfaVerifyRequestDto reqDto);

    /**
     * 验证MFA验证码
     *
     * @param reqDto MFA验证请求参数
     * @return 验证结果
     */
    Response<Boolean> verifyMfaCode(MfaVerifyRequestDto reqDto);
}