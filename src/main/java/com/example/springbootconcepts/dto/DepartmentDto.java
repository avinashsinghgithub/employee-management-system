package com.example.springbootconcepts.dto;

import com.example.springbootconcepts.beanValidators.validationGroups.OrderOnePost;
import com.example.springbootconcepts.validators.customValidators.ValidateDepartmentName;
import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class DepartmentDto {

    UUID id;
    @ValidateDepartmentName(groups = OrderOnePost.class)
    String departmentName;
    String description;
//    List<EmployeeDto> employees;
}
