package com.example.springbootconcepts.exceptionhandlers;

import com.example.springbootconcepts.exceptionHandlers.EmployeeErrorResponse;
import com.example.springbootconcepts.exceptionhandlers.exceptions.EmployeeAlreadyExists;
import com.example.springbootconcepts.exceptionhandlers.exceptions.EmployeeIdNotFoundException;
import com.example.springbootconcepts.exceptionhandlers.exceptions.EntityNotFoundException;
import com.example.springbootconcepts.exceptionhandlers.exceptions.EmployeeTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class EmployeeExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentException(MethodArgumentNotValidException exc) {
        Map<String, String> errorMap = new HashMap<>();
        exc.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorResponse> handleException(EntityNotFoundException exc) {
        EmployeeErrorResponse error = new EmployeeErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorResponse> handleException(EmployeeIdNotFoundException empId) {
        EmployeeErrorResponse error = new EmployeeErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(empId.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeErrorResponse> handleException(EmployeeAlreadyExists employeeAlreadyExists) {
        EmployeeErrorResponse errorResponse = new EmployeeErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(employeeAlreadyExists.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<EmployeeErrorResponse> handleException(EmployeeTypeException employeeTypeException) {
        EmployeeErrorResponse errorResponse = new EmployeeErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(employeeTypeException.getMessage());
        errorResponse.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
//        @ExceptionHandler
//        public ResponseEntity<EmployeeErrorResponse> constraintViolationException(ConstraintViolationException cve){
//            EmployeeErrorResponse error= new EmployeeErrorResponse();
//
//            error.setStatus(HttpStatus.BAD_REQUEST.value());
//            error.setMessage(cve.getMessage());
//            error.setTimeStamp(System.currentTimeMillis());
//
//            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//        }
}
