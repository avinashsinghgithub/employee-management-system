// package com.example.springbootconcepts.controllers;

// import com.example.springbootconcepts.dto.EmployeeDto;
// import com.example.springbootconcepts.services.employeeServices.EmployeeService;
// import com.example.springbootconcepts.services.employeeServices.EmployeeServiceImpl;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.ResponseEntity;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.List;

// @WebMvcTest(controllers = EmployeeController.class)
// public class EmployeeControllerTest2 {

//     @MockBean
//     private EmployeeService employeeService;
//     private EmployeeController employeeController;

//     @Autowired
//     private MockMvc mockMvc;

//     @Test
//     void test_getAllEmployees() {
//        // mockMvc.perform(get() "/employee")
//         ResponseEntity<List<EmployeeDto>> employees = employeeController.getAllEmployees();
//     }
// }
