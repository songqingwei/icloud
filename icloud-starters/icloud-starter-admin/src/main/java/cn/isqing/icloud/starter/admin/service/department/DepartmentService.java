package cn.isqing.icloud.starter.admin.service.department;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.admin.api.dto.DepartmentDto;

import java.util.List;

public interface DepartmentService {
    Response<List<DepartmentDto>> getAllDepartments();

    Response<Boolean> addDepartment(DepartmentDto departmentDto);

    Response<Boolean> updateDepartment(DepartmentDto departmentDto);

    Response<Boolean> deleteDepartment(Long departmentId);
}