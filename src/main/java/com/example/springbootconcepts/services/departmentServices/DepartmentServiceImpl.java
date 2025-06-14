package com.example.springbootconcepts.services.departmentServices;

import com.example.springbootconcepts.domains.Department;
import com.example.springbootconcepts.domains.Employee;
import com.example.springbootconcepts.dto.DepartmentDto;
import com.example.springbootconcepts.exceptionhandlers.exceptions.EmployeeAlreadyExists;
import com.example.springbootconcepts.exceptionhandlers.exceptions.EntityNotFoundException;
import com.example.springbootconcepts.mappers.DepartmentMapper;
import com.example.springbootconcepts.repos.DepartmentRepository;
import com.example.springbootconcepts.repos.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    @Autowired
    DepartmentMapper departmentMapper;

    @Override
    public DepartmentDto saveDepartment(DepartmentDto departmentDto) {
        Department department = departmentMapper.departmentDtoToDepartment(departmentDto);
        Iterable<Department> dept = departmentRepository.findAll();

        for (Department tempDept : dept) {
            if (tempDept.getDepartmentName().equals(departmentDto.getDepartmentName())) {
                throw new EmployeeAlreadyExists("Department already exists");
            }
        }
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.departmentToDepartmentDto(savedDepartment);
    }

    @Override
    public List<DepartmentDto> getDepartments() {
        Iterable<Department> deptIterable = departmentRepository.findAll();
            return ((List<Department>) deptIterable)
                    .stream()
                    .map(dept -> departmentMapper.departmentToDepartmentDto(dept))
                    .collect(Collectors.toList());
    }

    @Override
    public void deleteDepartments() {
        departmentRepository.deleteAll();
    }

    @Override
    public Map<UUID, String> assignDepartmentToEmployees(UUID deptID, List<UUID> empIds) {
        Department department = departmentRepository.findById(deptID).orElseThrow(() -> new EntityNotFoundException("Department does not exist with id: "+deptID));
        Map<UUID,String> result = new HashMap<>();
        for(UUID empId : empIds){
            try{
            Optional<Employee> employee = employeeRepository.findById(empId); //.orElseThrow(() -> new EntityNotFoundException("Employee not exist with id: "));
                if(!employee.isPresent()) {
                    result.put(empId,"invalid employee id");
                    continue;
                }
            if(employee.get().getDepartment()==null){
            employee.get().setDepartment(department);
            employeeRepository.save(employee.get());
            result.put(empId,"Department assigned successfully!");
            }else{
                result.put(empId,"Department already assigned");
                }
            }catch (Exception e){
                result.put(empId,"failed to assign Department!!");
                System.out.println("Error inserting employee with ID:"+empId);
                //throw e;
            }
        }
        return result;
    }
    @Override
    @Transactional
    public void assignDepartmentToAnEmployee(String deptId, String empId){
        Optional<Employee> employee = employeeRepository.findById(UUID.fromString(empId));
        Optional<Department> department = departmentRepository.findById(UUID.fromString(deptId));
        if(employee.isPresent() && department.isPresent()){
            employee.get().setDepartment(department.get());
        }
    }
}
