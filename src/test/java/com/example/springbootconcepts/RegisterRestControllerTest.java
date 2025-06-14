package com.example.springbootconcepts;

import com.example.springbootconcepts.controllers.EmployeeController;
import com.example.springbootconcepts.dto.AddressDto;
import com.example.springbootconcepts.dto.EmployeeDto;
import com.example.springbootconcepts.services.employeeServices.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.sql.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = EmployeeController.class,excludeAutoConfiguration = SecurityAutoConfiguration.class)
class RegisterRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    private EmployeeDto employee ;

    @BeforeEach
    public void  setup (){
    //    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
     //   validator = factory.getValidator();
        this.employee = EmployeeDto.builder()
                .id(UUID.randomUUID()).firstName("avinash")
                .lastName("test")
                .employeeType("permanent")
                .email("test@allstate.com")
                .joiningDate(new Date(2023,01,11))
                .address(AddressDto.builder()
                        .flatName("abc").build())
                .build();
     //   this.mockMvc = MockMvcBuilders.standaloneSetup(new EmployeeController(employeeService)).build();
    }
    @Test
    void testUpdateEmployee_firstNameOnly() throws Exception{

        String json = objectMapper.writeValueAsString(employee);
        mockMvc.perform(put("/api/employee").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andExpectAll(
                        status().isOk()
//                      , result -> assertEquals("resource not found", result.getResolvedException().getMessage()),
//                        content().string("test")
                );

    }

    @Test
    void test_get_employee() throws Exception{
        mockMvc.perform(get("/api/employee"))
                .andExpectAll(
                        status().isOk(),
                        content().string("[]")
                );
    }

    @Test
    void test_saveEmployee() throws Exception {
        String json = objectMapper.writeValueAsString(employee);
        ResultActions result = mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(json));
        MvcResult result2 = result.andReturn();
        MockHttpServletResponse response = result2.getResponse();
        String content = response.getContentAsString().replace(".","_");
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(content);

        String id = JsonPath.read(document, "$.id");
        String pinCode = JsonPath.read(document, "$.address_pinCode");
        String street = JsonPath.read(document, "$.address_street");
        String aptNum = JsonPath.read(document, "$.address_aptNum");


        assertEquals(id,"must be null");
        assertEquals(pinCode,"must not be null");
        assertEquals(street,"must not be null");
        assertEquals(aptNum,"must not be null");
//                .andExpectAll(
//                        status().isBadRequest(),
//                        //content().string("[]"),
//                        jsonPath("$.id", is("must be null")),
//                        jsonPath("$.address.pinCode", is("must be null")),
//                        jsonPath("$.address.street", is("must be null")),
//                        jsonPath("$.address.aptNum", is("must be null"))
//                );
    }
}