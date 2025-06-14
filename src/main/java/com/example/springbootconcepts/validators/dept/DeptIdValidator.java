package com.example.springbootconcepts.validators.dept;

import com.example.springbootconcepts.exceptionhandlers.exceptions.EmployeeIdNotFoundException;
import com.example.springbootconcepts.validators.customValidators.ValidateDeptID;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class DeptIdValidator implements ConstraintValidator<ValidateDeptID, UUID> {
    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
        if (id==null){
            return false;
        }
        else {
            return true;
        }
    }
}