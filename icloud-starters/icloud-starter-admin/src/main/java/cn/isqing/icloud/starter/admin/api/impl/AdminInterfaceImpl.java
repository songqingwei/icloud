package cn.isqing.icloud.starter.admin.api.impl;

import cn.isqing.icloud.common.api.dto.PageReqDto;
import cn.isqing.icloud.common.api.dto.PageResDto;
import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.admin.api.AdminInterface;
import cn.isqing.icloud.starter.admin.api.dto.*;
import cn.isqing.icloud.starter.admin.service.department.DepartmentService;
import cn.isqing.icloud.starter.admin.service.menu.MenuService;
import cn.isqing.icloud.starter.admin.service.role.RoleService;
import cn.isqing.icloud.starter.admin.service.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AdminInterfaceImpl implements AdminInterface {

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Override
    public Response<String> login(UserLoginRequestDto reqDto) {
        return userService.login(reqDto);
    }

    @Override
    public Response<Boolean> register(UserDto userDto) {
        return userService.register(userDto);
    }

    @Override
    public Response<UserDto> getUserById(Long userId) {
        return userService.getUserById(userId);
    }

    @Override
    public Response<Boolean> updateUser(UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @Override
    public Response<Boolean> deleteUser(Long userId) {
        return userService.deleteUser(userId);
    }

    @Override
    public Response<PageResDto<UserDto>> listUsers(PageReqDto<UserDto> req) {
        return userService.listUsers(req);
    }

    @Override
    public Response<List<RoleDto>> getUserRoles(Long userId) {
        Response<List<UserDto>> userRolesResponse = userService.getUserRoles(userId);
        // 将UserDto转换为RoleDto
        List<RoleDto> roleDtos = userRolesResponse.getData().stream().map(userDto -> {
            RoleDto roleDto = new RoleDto();
            roleDto.setId(userDto.getId());
            roleDto.setName(userDto.getUsername());
            return roleDto;
        }).collect(Collectors.toList());
        return Response.success(roleDtos);
    }

    @Override
    public Response<Boolean> assignUserRole(Long userId, Set<Long> roleIds) {
        return userService.assignUserRole(userId, roleIds);
    }

    @Override
    public Response<List<DepartmentDto>> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @Override
    public Response<Boolean> addDepartment(DepartmentDto departmentDto) {
        return departmentService.addDepartment(departmentDto);
    }

    @Override
    public Response<Boolean> updateDepartment(DepartmentDto departmentDto) {
        return departmentService.updateDepartment(departmentDto);
    }

    @Override
    public Response<Boolean> deleteDepartment(Long departmentId) {
        return departmentService.deleteDepartment(departmentId);
    }

    @Override
    public Response<List<RoleDto>> getAllRoles() {
        return roleService.getAllRoles();
    }

    @Override
    public Response<Boolean> addRole(RoleDto roleDto) {
        return roleService.addRole(roleDto);
    }

    @Override
    public Response<Boolean> updateRole(RoleDto roleDto) {
        return roleService.updateRole(roleDto);
    }

    @Override
    public Response<Boolean> deleteRole(Long roleId) {
        return roleService.deleteRole(roleId);
    }

    @Override
    public Response<List<MenuDto>> getRoleMenus(Long roleId) {
        return menuService.getMenusByRoleId(roleId);
    }

    @Override
    public Response<Boolean> assignRoleMenu(Long roleId, Set<Long> menuIds) {
        return roleService.assignRoleMenu(roleId, menuIds);
    }

    @Override
    public Response<List<MenuDto>> getAllMenus() {
        return menuService.getAllMenus();
    }

    @Override
    public Response<Boolean> addMenu(MenuDto menuDto) {
        return menuService.addMenu(menuDto);
    }

    @Override
    public Response<Boolean> updateMenu(MenuDto menuDto) {
        return menuService.updateMenu(menuDto);
    }

    @Override
    public Response<Boolean> deleteMenu(Long menuId) {
        return menuService.deleteMenu(menuId);
    }
    
    @Override
    public Response<String> generateMfaQRCode(Long userId) {
        return userService.generateMfaQRCode(userId);
    }

    @Override
    public Response<Boolean> enableMfa(MfaVerifyRequestDto reqDto) {
        return userService.enableMfa(reqDto);
    }

    @Override
    public Response<Boolean> verifyMfaCode(MfaVerifyRequestDto reqDto) {
        return userService.verifyMfaCode(reqDto);
    }
}