package com.example.springbootconcepts.mappers;

import com.example.springbootconcepts.domains.Employee;
import com.example.springbootconcepts.dto.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface EmployeeMapper {

    EmployeeDto employeeToEmployeeDto(Employee employee);


//    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
//    @Mapping(target = "address.id", expression = "java(UUID.randomUUID())")
    Employee employeeDtoToEmployee(EmployeeDto employeeDto);
}
