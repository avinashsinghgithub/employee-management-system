package com.example.springbootconcepts.validators.customValidators;

import com.example.springbootconcepts.validators.EmployeeIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Constraint(validatedBy = EmployeeIdValidator.class)
public @interface ValidateID {
    String message() default " Employee ID not found ";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
