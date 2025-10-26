package com.example.springbootconcepts.controllers;

import com.example.springbootconcepts.domains.Employee;
import com.example.springbootconcepts.dto.EmployeeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EmployeeController_IT {

    @Autowired
    EmployeeController controller;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void getEmployees_successfully (){
        ResponseEntity<List<EmployeeDto>> response  = testRestTemplate
                .exchange("/api/employee",
                        HttpMethod.GET,null,
                        new ParameterizedTypeReference<List<EmployeeDto>>() {});
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void postEmployee_successfully (){
        var newEmployee = EmployeeDto.builder().firstName("avinash").build();
        ResponseEntity<Void> response = testRestTemplate.postForEntity("/api/employee",newEmployee,Void.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
