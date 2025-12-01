package cn.isqing.icloud.starter.admin.dao.mapper;

import cn.isqing.icloud.starter.admin.dao.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface RoleMapper {
    int insert(Role role);

    int update(Role role);

    int deleteById(@Param("id") Long id);

    Role selectById(@Param("id") Long id);

    List<Role> selectList(Role role);

    List<Role> selectAll();

    List<Role> selectByUserId(@Param("userId") Long userId);

    int batchInsertUserRole(@Param("userId") Long userId, @Param("roleIds") Set<Long> roleIds);

    int deleteByUserId(@Param("userId") Long userId);
}