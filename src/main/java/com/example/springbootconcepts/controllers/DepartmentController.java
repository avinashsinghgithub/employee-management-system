package com.example.springbootconcepts.controllers;

import com.example.springbootconcepts.validators.groups.*;
import com.example.springbootconcepts.dto.AssignDept;
import com.example.springbootconcepts.dto.DepartmentDto;
import com.example.springbootconcepts.services.departmentServices.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/department")
public class DepartmentController {

    private final DepartmentService departmentService;
    @PostMapping()
    public ResponseEntity<Object> saveDepartment(@RequestBody @Validated(OrderOnePost.class) DepartmentDto departmentDto) {
        return new ResponseEntity<>(departmentService.saveDepartment(departmentDto), HttpStatus.OK);
    }
    @GetMapping()
    public ResponseEntity<Object> getDepartments() {
        return new ResponseEntity<>(departmentService.getDepartments(), HttpStatus.OK);
    }
    @PutMapping("/{deptId}/employee/{empId}")
    @ResponseStatus(HttpStatus.OK)
    public void assignDeptToEmp(@PathVariable String empId, @PathVariable String deptId) {
        departmentService.assignDepartmentToAnEmployee(empId,deptId);
    }
    @DeleteMapping()
    public ResponseEntity<Object> deleteDepartment() {
        departmentService.deleteDepartments();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/assignment")
    public ResponseEntity<Map<UUID,String>> AssignDepartment(@RequestBody AssignDept request) {
        Map<UUID,String> map = departmentService.assignDepartmentToEmployees(request.getId(),request.getEmployeesIDs());
        return ResponseEntity.ok(map);
    }
}
