package com.example.springbootconcepts.config;

import com.example.springbootconcepts.dto.EmployeeDto;
import com.example.springbootconcepts.services.employeeServices.EmployeeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.InputStream;
import java.util.List;
@Component
@RequiredArgsConstructor
@Slf4j
public class AppInitializer implements CommandLineRunner {
    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        Resource resource = new ClassPathResource("seed-data.json");
        InputStream inputStream = resource.getInputStream();
        List<EmployeeDto> employeeDtos =  objectMapper.readValue(inputStream, new TypeReference<List<EmployeeDto>>() {
        });

        if(employeeService.getAllEmployees().size()<=10){
            employeeDtos.stream().forEach(employeeService::saveEmployee);
        }else{
            log.info("Employees are added to DB already");
        }


    }
}
