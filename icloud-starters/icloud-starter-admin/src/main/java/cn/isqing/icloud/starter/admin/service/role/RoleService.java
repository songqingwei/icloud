package cn.isqing.icloud.starter.admin.service.role;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.admin.api.dto.RoleDto;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Response<List<RoleDto>> getAllRoles();

    Response<Boolean> addRole(RoleDto roleDto);

    Response<Boolean> updateRole(RoleDto roleDto);

    Response<Boolean> deleteRole(Long roleId);

    Response<Boolean> assignRoleMenu(Long roleId, Set<Long> menuIds);
}