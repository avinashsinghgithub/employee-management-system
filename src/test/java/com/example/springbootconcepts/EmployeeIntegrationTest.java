package com.example.springbootconcepts;

import com.example.springbootconcepts.controllers.EmployeeController;
import com.example.springbootconcepts.dto.EmployeeDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class EmployeeIntegrationTest {

    @Autowired
    private EmployeeController subject;


    @BeforeEach
    void testPreparation(){
        ResponseEntity response  = subject.getAllEmployees();
        List<EmployeeDto> data =  ( List<EmployeeDto> )response.getBody();
        Optional  <EmployeeDto> employee =
                data.stream().filter((EmployeeDto item ) -> item.getFirstName().equals("avinash21")).findFirst();
        if(employee.isPresent())
        subject.deleteEmployeeById(employee.get().getId());

    }
    @Test
    void saveEmployeeTest(){
        EmployeeDto employee = EmployeeDto.builder()
                .firstName("avinash21")
                .lastName("singh120")
                .build();

        ResponseEntity<EmployeeDto> entity =  subject.saveEmployee(employee);
        Assertions.assertNotNull(entity);
    }
}