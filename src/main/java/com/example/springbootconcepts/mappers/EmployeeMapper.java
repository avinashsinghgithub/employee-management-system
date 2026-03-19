package com.example.springbootconcepts.mappers;

import com.example.springbootconcepts.domains.Employee;
import com.example.springbootconcepts.domains.Image;
import com.example.springbootconcepts.dto.EmployeeDto;
import com.example.springbootconcepts.dto.ImageInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface EmployeeMapper {

    EmployeeDto employeeToEmployeeDto(Employee employee);

    // MapStruct will use these element mapping methods to map collections of images.
    @Mapping(source = "fileName", target = "filename")
    ImageInfo imageToImageInfo(Image image);

    @Mapping(source = "filename", target = "fileName")
    Image imageInfoToImage(ImageInfo imageInfo);

    Employee employeeDtoToEmployee(EmployeeDto employeeDto);
}
