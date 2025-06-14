package com.example.springbootconcepts.services;

import com.example.springbootconcepts.beanValidators.validationGroups.OrderOnePut;
import com.example.springbootconcepts.dto.EmployeeDto;
import com.example.springbootconcepts.exceptionhandlers.exceptions.EntityNotFoundException;
import com.example.springbootconcepts.beanValidators.validationGroups.OrderOnePost;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class EmployeeServiceValidator {
    private final Validator requestValidator;

    public EmployeeServiceValidator(Validator requestValidator) {

        this.requestValidator = requestValidator;
    }

    public void validateEditQuoteRequestForV3(EmployeeDto quoteRequest) {
        Set<ConstraintViolation<EmployeeDto>> violationForEditOnUpdate = requestValidator.validate(quoteRequest, OrderOnePut.class);
        if (!violationForEditOnUpdate.isEmpty()) {
            throw new ConstraintViolationException(violationForEditOnUpdate);
        }
    }
    public boolean validateRequest(EmployeeDto quoteRequest) {
        Set<ConstraintViolation<EmployeeDto>> violationForOnCreate = requestValidator.validate(quoteRequest, OrderOnePost.class);
        if (violationForOnCreate.isEmpty()) {
            return true;
        }
        else{
            throw new EntityNotFoundException("id should not be in request");
        }

    }
}
