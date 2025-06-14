package com.example.springbootconcepts.validators.customValidators;

import com.example.springbootconcepts.validators.EmployeeTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.stereotype.Component;
import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Constraint(validatedBy = EmployeeTypeValidator.class)
public @interface ValidateEmployeeType {
    String message() default "Invalid Employee Type:It should be permanent,contractor or Interns";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
