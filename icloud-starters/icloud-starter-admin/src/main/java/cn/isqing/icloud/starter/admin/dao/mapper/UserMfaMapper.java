package cn.isqing.icloud.starter.admin.dao.mapper;

import cn.isqing.icloud.starter.admin.dao.entity.UserMfa;
import org.apache.ibatis.annotations.Param;

public interface UserMfaMapper {
    int insert(UserMfa userMfa);

    int update(UserMfa userMfa);

    int deleteByUserId(@Param("userId") Long userId);

    UserMfa selectByUserId(@Param("userId") Long userId);
}