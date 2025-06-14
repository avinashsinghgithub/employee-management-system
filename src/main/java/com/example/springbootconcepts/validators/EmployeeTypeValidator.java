package com.example.springbootconcepts.validators;
import com.example.springbootconcepts.exceptionhandlers.exceptions.EmployeeTypeException;
import com.example.springbootconcepts.validators.customValidators.ValidateEmployeeType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class EmployeeTypeValidator implements ConstraintValidator<ValidateEmployeeType, String> {
    @Override
    public boolean isValid(String employeeType, ConstraintValidatorContext context) {
        List<String> employeeTypes = Arrays.asList("permanent", "contractor","interns");
        if (employeeTypes.contains(employeeType)== false){
            //throw new EmployeeTypeException("Invalid Employee Type");
            return false;
        }
        else{
           return employeeTypes.contains(employeeType);
        }
    }
}
