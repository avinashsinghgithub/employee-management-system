package com.example.springbootconcepts.controllers;

import com.example.springbootconcepts.validators.groups.*;
import com.example.springbootconcepts.dto.EmployeeDto;
import com.example.springbootconcepts.services.employeeServices.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EmployeeController {
    private final EmployeeService employeeService;
    @PostMapping("/employee")
    public ResponseEntity<EmployeeDto> saveEmployee(@RequestBody @Validated(OrderOnePost.class) EmployeeDto employeeDto) {
        return new ResponseEntity<>(employeeService.saveEmployee(employeeDto), HttpStatus.OK);
    }
    @GetMapping("/employee")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }
    @PutMapping("/employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> editEmployee(@RequestBody @Validated(OrderOnePut.class) EmployeeDto updatedEmployee) {

        return new ResponseEntity<>(employeeService.editEmployee(updatedEmployee),HttpStatus.OK);
    }
    @DeleteMapping("/employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteAllEmployee() {
        employeeService.deleteAllEmployee();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/employee/paging")
    public ResponseEntity<Object> getAllEmployeesByPaging(@RequestParam(value = "pageNumber",required = false) Integer pageNumber,
                                                  @RequestParam(value = "pageSize",required = false) Integer pageSize,
                                                  @RequestParam(value = "sortBy",required = false) String field) {
        return new ResponseEntity<>(employeeService.getAllEmployeesByPaging(pageNumber,pageSize,field), HttpStatus.OK);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Object> getEmployeeById(@PathVariable UUID id) {
        return new ResponseEntity<>(employeeService.getEmployeeById(id), HttpStatus.OK);
    }

    @GetMapping("/employee/name")
    public ResponseEntity<Object> getEmployeeByFirstNameOrLastName(@RequestParam String name) {
        return new ResponseEntity<>(employeeService.getEmployeeByFirstNameOrLastName(name),HttpStatus.OK);
    }
    @GetMapping("/employee/lastname")
    public ResponseEntity<Object> getEmployeeByFirstName(@RequestParam String lastName) {
        return new ResponseEntity<>(employeeService.getEmployeeByLastName(lastName),HttpStatus.OK);
    }

    @DeleteMapping("/employee/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> deleteEmployeeById(@PathVariable UUID id) {
        employeeService.deleteEmployeeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/employee/remove")
    public ResponseEntity<Object> deleteAllEmployeeByYear(@RequestParam String joiningYear) {
        employeeService.deleteAllEmployeeByYear(joiningYear);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}