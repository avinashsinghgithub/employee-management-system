package com.example.springbootconcepts.mappers;
import com.example.springbootconcepts.domains.Employee;
import com.example.springbootconcepts.dto.EmployeeDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EmployeeMapperTest {

    EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);
    @Test
    public void entityToDto() {
        Employee employee = new Employee();
        employee.setFirstName("Nimitt");
        employee.setEmployeeType("Permanent");

        EmployeeDto employeeDto = mapper.employeeToEmployeeDto(employee);

        assertEquals(employeeDto.getFirstName(), employee.getFirstName());
        assertEquals(employeeDto.getLastName(), employee.getLastName());
    }
    @Test
    public void givenEmployeeDTOwithDiffNametoEmployee_whenMaps_thenCorrect() {
        EmployeeDto dto = EmployeeDto.builder()
                .firstName("Nimitt")
                .lastName("Verma").build();

        Employee entity = mapper.employeeDtoToEmployee(dto);

        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
    }
}