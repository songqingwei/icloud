package cn.isqing.icloud.starter.admin.dao.mapper;

import cn.isqing.icloud.starter.admin.dao.entity.User;
import cn.isqing.icloud.starter.admin.dao.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int insert(User user);

    int update(User user);

    int deleteById(@Param("id") Long id);

    User selectById(@Param("id") Long id);

    User selectByUsername(@Param("username") String username);

    List<User> selectList(User user);

    List<User> selectPage(@Param("user") User user, @Param("offset") int offset, @Param("limit") int limit);

    int count(User user);

    List<Role> selectRolesByUserId(@Param("userId") Long userId);
}