package com.example.springbootconcepts.exceptionhandlers.exceptions;

public class EmployeeTypeException extends RuntimeException{
    public EmployeeTypeException(String message){
        super(message);
    }
}
