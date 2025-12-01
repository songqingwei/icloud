package cn.isqing.icloud.starter.admin.dao.mapper;

import cn.isqing.icloud.starter.admin.dao.entity.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentMapper {
    int insert(Department department);

    int update(Department department);

    int deleteById(@Param("id") Long id);

    Department selectById(@Param("id") Long id);

    List<Department> selectList(Department department);

    List<Department> selectAll();
}