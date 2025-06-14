package com.example.springbootconcepts.exceptionhandlers.exceptions;

public class EmployeeAlreadyExists extends RuntimeException{
    public EmployeeAlreadyExists(String message){
        super(message);
    }
}
