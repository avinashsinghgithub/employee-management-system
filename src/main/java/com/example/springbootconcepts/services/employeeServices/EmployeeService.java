package com.example.springbootconcepts.services.employeeServices;
import com.example.springbootconcepts.dto.EmployeeDto;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeService {
    EmployeeDto saveEmployee(EmployeeDto employeeDto);
    Optional<EmployeeDto> getEmployeeById(UUID id);
    Optional<EmployeeDto> getEmployeeByFirstNameOrLastName(String firstName);
    Optional<EmployeeDto> getEmployeeByLastName(String lastName);
    void deleteEmployeeById(UUID id);
    void deleteAllEmployee();
    void deleteAllEmployeeByYear(String joiningYear);
    EmployeeDto editEmployee(EmployeeDto updatedEmployee);
    List<EmployeeDto> getAllEmployees();
    Page<EmployeeDto> getAllEmployeesByPaging(int pageNumber, int pageSize, String field);
}
