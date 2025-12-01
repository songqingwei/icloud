package cn.isqing.icloud.starter.admin.service.role.impl;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.admin.api.dto.RoleDto;
import cn.isqing.icloud.starter.admin.dao.entity.Role;
import cn.isqing.icloud.starter.admin.dao.mapper.RoleMapper;
import cn.isqing.icloud.starter.admin.service.role.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Response<List<RoleDto>> getAllRoles() {
        List<Role> roles = roleMapper.selectAll();
        List<RoleDto> roleDtos = roles.stream().map(role -> {
            RoleDto roleDto = new RoleDto();
            BeanUtils.copyProperties(role, roleDto);
            return roleDto;
        }).collect(Collectors.toList());
        return Response.success(roleDtos);
    }

    @Override
    public Response<Boolean> addRole(RoleDto roleDto) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDto, role);
        roleMapper.insert(role);
        return Response.success(true);
    }

    @Override
    public Response<Boolean> updateRole(RoleDto roleDto) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDto, role);
        roleMapper.update(role);
        return Response.success(true);
    }

    @Override
    public Response<Boolean> deleteRole(Long roleId) {
        roleMapper.deleteById(roleId);
        return Response.success(true);
    }

    @Override
    public Response<Boolean> assignRoleMenu(Long roleId, Set<Long> menuIds) {
        // 实现分配角色菜单逻辑
        return Response.success(true);
    }
}