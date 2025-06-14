package com.example.springbootconcepts.controllers;

import com.example.springbootconcepts.dto.AddressDto;
import com.example.springbootconcepts.dto.EmployeeDto;
import com.example.springbootconcepts.services.employeeServices.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.nio.charset.Charset;
import java.sql.Date;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = EmployeeController.class)
@AutoConfigureMockMvc
//@SpringBootTest
public class EmployeeControllerUnitTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService mockEmployeeService;

    @Autowired
    private ObjectMapper objectMapper;

//    @BeforeEach
//    void setup() {
//        this.mockMvc = MockMvcBuilders.standaloneSetup(new EmployeeController(mockEmployeeService)).build();
//        this.objectMapper = new ObjectMapper();
//
//    }
    @Test
    @WithMockUser(roles = "USER")
    void testGetAllEmployees(){

        try {
            mockMvc.perform(get("/api/employee"))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType("application/json")
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void testSaveEmployee_Name_Emp_type_joining_date_Only() throws Exception{

        EmployeeDto employee = EmployeeDto
                .builder()
                .firstName("avinash")
                .lastName("sharma")
                .employeeType("permanent")
                .joiningDate(Date.valueOf("2024-01-01"))
                .email("test@testmail.com")
                .address(AddressDto.builder()
                        .aptNum("XYZ")
                        .flatName("Tulsi")
                        .pinCode("560100")
                        .street("Neeladri")
                        .build())
                .build();
        String json = objectMapper.writeValueAsString(employee);
        mockMvc.perform(post("/api/employee")
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                    .andExpectAll(
                            status().isOk()
                    );

    }


    @Test
    @WithMockUser(roles = "USER")
    void testSaveEmployee_lastNameOnly() throws Exception{
        EmployeeDto employee = EmployeeDto.builder().lastName("avinash").build();
        String json = objectMapper.writeValueAsString(employee);
        mockMvc.perform(post("/api/employee",employee)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpectAll(
                        status().isBadRequest()
                );

    }
    @Test
    @WithMockUser(roles = "USER")
    void testSaveEmployee_firstNameOnly() throws Exception{
        EmployeeDto employee = EmployeeDto.builder().firstName("avinash").build();
        String json = objectMapper.writeValueAsString(employee);
        mockMvc.perform(post("/api/employee",employee)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpectAll(
                        status().isBadRequest(),
                        content().string("")
                );

    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateEmployee_firstNameOnly() throws Exception{
        EmployeeDto employee = EmployeeDto.builder().firstName("avinash").build();
        String json = objectMapper.writeValueAsString(employee);
        mockMvc.perform(put("/api/employee",employee)
                        .with(csrf().asHeader())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json))
                .andExpectAll(
                        status().isBadRequest(),
                        content().string("{\"lastName\":\"Last Name should not be null\",\"employeeType\":\"Invalid Employee Type:It should be permanent,contractor or Interns\",\"address\":\"Address should not be null\",\"joiningDate\":\"Joining Date should not be null\",\"email\":\"email should not be null\"}")
                );

    }
    @Test
    @WithMockUser(roles = "USER")
    void testUpdateEmployee_firstName_id_emp_type() throws Exception{
        EmployeeDto employee = EmployeeDto.builder().firstName("avinash")
                .id(UUID.fromString("ba52edda-b5f3-48e1-8415-a74144d56ba9"))
                .employeeType("permanent").build();
        String json = objectMapper.writeValueAsString(employee);
        mockMvc.perform(put("/api/employee",employee).contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpectAll(
                        status().isOk(),
                        content().string("")
                );

    }
}
