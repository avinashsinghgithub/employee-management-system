package com.example.springbootconcepts.services;

import com.example.springbootconcepts.domains.Employee;
import com.example.springbootconcepts.dto.EmployeeDto;
import com.example.springbootconcepts.mappers.EmployeeMapper;
import com.example.springbootconcepts.repos.EmployeeRepository;
import com.example.springbootconcepts.services.employeeServices.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceUnitTests {

    @Mock
    private EmployeeRepository repository;

    @Mock
    EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl service;

    @Test
    public void test_save_employee (){

        Employee employeeFromDB= Employee.builder()
                .firstName("testName")
                .lastName("singh")
                .build();
        EmployeeDto employeeToSave= EmployeeDto.builder()
                .firstName("testName")
                .lastName("singh")
                .build();

        when(employeeMapper.employeeDtoToEmployee(any())).thenReturn(employeeFromDB);
       // when(repository.findAll()).thenReturn(new ArrayList<>());
        when(repository.save(any())).thenReturn(employeeFromDB);
        when(employeeMapper.employeeToEmployeeDto(any())).thenReturn(employeeToSave);
        EmployeeDto result = service.saveEmployee(employeeToSave);
        Assertions.assertNotNull(result);
    }
}
