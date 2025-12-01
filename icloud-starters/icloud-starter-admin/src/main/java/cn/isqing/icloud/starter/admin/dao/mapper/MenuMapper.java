package cn.isqing.icloud.starter.admin.dao.mapper;

import cn.isqing.icloud.starter.admin.dao.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface MenuMapper {
    int insert(Menu menu);

    int update(Menu menu);

    int deleteById(@Param("id") Long id);

    Menu selectById(@Param("id") Long id);

    List<Menu> selectList(Menu menu);

    List<Menu> selectAll();

    List<Menu> selectByRoleId(@Param("roleId") Long roleId);

    int batchInsertRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") Set<Long> menuIds);

    int deleteByRoleId(@Param("roleId") Long roleId);
}