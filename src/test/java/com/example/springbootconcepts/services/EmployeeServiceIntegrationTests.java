// package com.example.springbootconcepts.services;

// import com.example.springbootconcepts.dto.EmployeeDto;
// import com.example.springbootconcepts.services.employeeServices.EmployeeService;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.ResponseEntity;

// import java.util.List;
// import java.util.Optional;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
// public class EmployeeServiceIntegrationTests {

//     @Autowired
//     private EmployeeService subject;
//     @BeforeEach
//     void testPreparation(){
//         List<EmployeeDto> data  = subject.getAllEmployees();
//         Optional<EmployeeDto> employee =
//                 data.stream().filter((EmployeeDto item ) -> item.getFirstName().equals("subject")).findFirst();
//         if(employee.isPresent())
//             subject.deleteEmployeeById(employee.get().getId());

//     }
//     @Test
//     public void test_Get_All_Employees (){

//         List result = subject.getAllEmployees();
//         Assertions.assertNotNull(result);
//     }

//     @Test
//     public void test_save_employee (){
//         EmployeeDto employeeToSave= EmployeeDto.builder()
//                 .firstName("subject")
//                 .lastName("singh")
//                 .build();
//         EmployeeDto result = subject.saveEmployee(employeeToSave);
//         Assertions.assertNotNull(result);
//     }

// }
