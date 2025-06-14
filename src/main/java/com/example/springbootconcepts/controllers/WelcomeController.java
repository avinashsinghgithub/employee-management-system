package com.example.springbootconcepts.controllers;

import com.example.springbootconcepts.domains.Employee;
import com.example.springbootconcepts.dto.EmployeeDto;
import com.example.springbootconcepts.mappers.EmployeeMapper;
import com.example.springbootconcepts.repos.EmployeeRepository;
import com.example.springbootconcepts.services.employeeServices.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.UUID;

@Controller
public class WelcomeController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeMapper employeeMapper;

    @GetMapping("/")
    public String defaultPath(Model model){

        return "redirect:/employees";
    }

    //handler method to handle list employees and return mode and view
    @GetMapping("/employees")
    public String ListEmployee(Model model){
        model.addAttribute("employees",employeeService.getAllEmployees());
        return "employees";
    }
//    @RequestMapping(path = "/employees/{pageNo}", method = RequestMethod.POST)
////    @GetMapping("/employees/{pageNo}")
//    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,@RequestParam("sortField") String sortField,Model model) {
//        int pageSize = 5;
//        model.addAttribute("employees",employeeService.getAllEmployees());
//        Page<EmployeeDto> page = employeeService.getAllEmployeesByPaging(pageNo, pageSize,sortField);
//        model.addAttribute("currentPage", pageNo);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems", page.getTotalElements());
//        model.addAttribute("sortField", sortField);
//        return "employees";
//    }
//    @GetMapping("/")
//    public String viewHomePage(Model model) {
//        return findPaginated(1, "firstName", model);
//    }

    @GetMapping("/employees/new")
    public String createEmployeeForm(Model model){
        //create Employee obj to hold emp form data
        Employee employee = new Employee();
        model.addAttribute("employee",employee);
        return "create_employee";
    }

    @PostMapping("/employees")
    public String saveEmployee(@ModelAttribute("employee") EmployeeDto employeeDto){
        employeeService.saveEmployee(employeeDto);
        return "redirect:/employees";
    }

    @GetMapping("/employees/edit/{id}")
    public String editEmployeeForm(@PathVariable UUID id,Model model){
//        model.addAttribute("employee",employeeService.getEmployeeById(id));
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        EmployeeDto toBeEditedEmployee = employeeMapper.employeeToEmployeeDto(employee);
        model.addAttribute("employee", toBeEditedEmployee);
        return "edit_employee";
    }

    @PostMapping("/employees/{id}")
    public String updateEmployee(@PathVariable("id") UUID id, @Valid EmployeeDto employee, BindingResult result){
        if (result.hasErrors()) {
            employee.setId(id);
            return "edit_employee";
        }
        employeeService.editEmployee(employee);
        return "redirect:/employees";
    }

    //handler method to handle delete employee request
    @GetMapping("/employees/{id}")
    public String deleteEmployeeById(@PathVariable UUID id,Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        employeeRepository.delete(employee);
        return "redirect:/employees";
    }
}
