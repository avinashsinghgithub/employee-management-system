package com.example.springbootconcepts.mappers;

import com.example.springbootconcepts.domains.Department;
import com.example.springbootconcepts.dto.DepartmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface DepartmentMapper {
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    DepartmentDto departmentToDepartmentDto(Department department);

//    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    Department departmentDtoToDepartment(DepartmentDto departmentDto);

}
