package com.example.springbootconcepts.validators.dept;
import com.example.springbootconcepts.validators.customValidators.ValidateDepartmentName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DepartmentValidator implements ConstraintValidator<ValidateDepartmentName, String> {
    @Override
    public boolean isValid(String departmentName, ConstraintValidatorContext context) {
        List<String> departmentNames = Arrays.asList("accountancy", "sales","marketing","tech-team");
        return  departmentNames.contains(departmentName);
    }
}
