package cn.isqing.icloud.starter.admin.service.department.impl;

import cn.isqing.icloud.common.api.dto.Response;
import cn.isqing.icloud.starter.admin.api.dto.DepartmentDto;
import cn.isqing.icloud.starter.admin.dao.entity.Department;
import cn.isqing.icloud.starter.admin.dao.mapper.DepartmentMapper;
import cn.isqing.icloud.starter.admin.service.department.DepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public Response<List<DepartmentDto>> getAllDepartments() {
        List<Department> departments = departmentMapper.selectAll();
        List<DepartmentDto> departmentDtos = departments.stream().map(department -> {
            DepartmentDto departmentDto = new DepartmentDto();
            BeanUtils.copyProperties(department, departmentDto);
            return departmentDto;
        }).collect(Collectors.toList());
        return Response.success(departmentDtos);
    }

    @Override
    public Response<Boolean> addDepartment(DepartmentDto departmentDto) {
        Department department = new Department();
        BeanUtils.copyProperties(departmentDto, department);
        departmentMapper.insert(department);
        return Response.success(true);
    }

    @Override
    public Response<Boolean> updateDepartment(DepartmentDto departmentDto) {
        Department department = new Department();
        BeanUtils.copyProperties(departmentDto, department);
        departmentMapper.update(department);
        return Response.success(true);
    }

    @Override
    public Response<Boolean> deleteDepartment(Long departmentId) {
        departmentMapper.deleteById(departmentId);
        return Response.success(true);
    }
}