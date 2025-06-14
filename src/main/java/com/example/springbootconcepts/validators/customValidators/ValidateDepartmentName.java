package com.example.springbootconcepts.validators.customValidators;

import com.example.springbootconcepts.validators.dept.DepartmentValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
@Constraint(validatedBy = DepartmentValidator.class)
public @interface ValidateDepartmentName {
    String message() default "Invalid department name:It should be accountancy,sales,marketing or tech-team";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
