package com.example.springbootconcepts.validators;

import com.example.springbootconcepts.validators.customValidators.ValidateID;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmployeeIdValidator implements ConstraintValidator<ValidateID, UUID> {
    @Override
    public boolean isValid(UUID id, ConstraintValidatorContext context) {
        if (id==null){
            //throw new EmployeeIdNotFoundException("Id not Found");
            return false;
        }
        else {
            return true;
        }
    }
}