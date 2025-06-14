package com.example.springbootconcepts.services.employeeServices;
import com.example.springbootconcepts.domains.Address;
import com.example.springbootconcepts.domains.Employee;
import com.example.springbootconcepts.dto.EmployeeDto;
import com.example.springbootconcepts.exceptionhandlers.exceptions.EmployeeAlreadyExists;
import com.example.springbootconcepts.exceptionhandlers.exceptions.EntityNotFoundException;
import com.example.springbootconcepts.mappers.EmployeeMapper;
import com.example.springbootconcepts.repos.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeMapper.employeeDtoToEmployee(employeeDto);

        Iterable<Employee> emp = employeeRepository.findAll();

        for (Employee tempEmp : emp) {
            if (tempEmp.getFirstName().equals(employeeDto.getFirstName()) && tempEmp.getLastName().equals(employeeDto.getLastName())) {
                throw new EmployeeAlreadyExists("Employee already exists");
            }
        }
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.employeeToEmployeeDto(savedEmployee);
    }

    @Override
    public Optional<EmployeeDto> getEmployeeById(UUID id) {
        Optional<Employee> savedEmployee = employeeRepository.findById(id);
        if (!savedEmployee.isPresent()) {
            throw new EntityNotFoundException("Employee not found with this id:");
        } else {
            return Optional.of(employeeMapper.employeeToEmployeeDto(savedEmployee.get()));
        }
    }

    @Override
    public Optional<EmployeeDto> getEmployeeByFirstNameOrLastName(String name) {
        Optional<Employee> employeeFirstName = employeeRepository.findByFirstNameOrLastName(name);
        if (!employeeFirstName.isPresent()) {
            throw new EntityNotFoundException("No Employee with the given firstName");
        } else {
            return Optional.of(employeeMapper.employeeToEmployeeDto(employeeFirstName.get()));
        }
    }

    @Override
    public Optional<EmployeeDto> getEmployeeByLastName(String lastName) {
        Optional<Employee> employeeFirstName = employeeRepository.findByLastName(lastName);
        if (!employeeFirstName.isPresent()) {
            throw new EntityNotFoundException("No Employee with the given firstName");
        } else {
            return Optional.of(employeeMapper.employeeToEmployeeDto(employeeFirstName.get()));
        }
    }

    @Override
    public void deleteEmployeeById(UUID id) {
        Optional<Employee> employeeSaved = employeeRepository.findById(id);
        if (!employeeSaved.isPresent()) {
            throw new EntityNotFoundException("No Employee with the given lastName");
        } else {
            employeeRepository.deleteById(id);
        }
    }

    @Override
    public void deleteAllEmployee() {
        employeeRepository.deleteAll();
    }

    @Override
    public void deleteAllEmployeeByYear(String joiningYear) {
        employeeRepository.deleteByJoiningYear(joiningYear);
    }

    @Override
    public EmployeeDto editEmployee(EmployeeDto updatedEmployee) {
        Employee employee = employeeRepository.findById(updatedEmployee.getId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not exist with id: " + updatedEmployee.getId()));
        if (!employee.getEmail().equalsIgnoreCase(updatedEmployee.getEmail())) {
            throw new EntityNotFoundException("email edit is not allowed");
        }

        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setLastName(updatedEmployee.getLastName());

        employee.setEmployeeType(updatedEmployee.getEmployeeType());
        employee.setJoiningDate(updatedEmployee.getJoiningDate());
        Address address = employee.getAddress();
        if(updatedEmployee.getAddress() != null) {
            address.setAptNum(updatedEmployee.getAddress().getAptNum());
            address.setPinCode(updatedEmployee.getAddress().getPinCode());
            address.setStreet(updatedEmployee.getAddress().getStreet());
            address.setFlatName(updatedEmployee.getAddress().getFlatName());
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.employeeToEmployeeDto(savedEmployee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        Iterable<Employee> savedEmployees = employeeRepository.findAll();
            return ((List<Employee>) savedEmployees)
                    .stream()
                    .map(employee -> employeeMapper.employeeToEmployeeDto(employee))
                    .collect(Collectors.toList());
    }

    @Override
    public Page<EmployeeDto> getAllEmployeesByPaging(int pageNumber, int pageSize, String field) {
        Page<Employee> emp = employeeRepository.findAll(PageRequest.of(pageNumber,pageSize).withSort(Sort.by(field)));
        Page<EmployeeDto> employeeDtoPage =  emp.map(employee -> employeeMapper.employeeToEmployeeDto(employee));
        return employeeDtoPage;
    }
}
