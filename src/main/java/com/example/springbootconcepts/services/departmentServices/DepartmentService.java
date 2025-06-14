package com.example.springbootconcepts.services.departmentServices;

import com.example.springbootconcepts.dto.DepartmentDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface DepartmentService {
    DepartmentDto saveDepartment(DepartmentDto departmentDto);
    List<DepartmentDto> getDepartments();
    void deleteDepartments();
    Map<UUID,String> assignDepartmentToEmployees(UUID deptID, List<UUID> empIds);
    void assignDepartmentToAnEmployee(String empId, String deptId);
}
