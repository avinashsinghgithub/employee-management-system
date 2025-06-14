package com.example.springbootconcepts.dto;

import com.example.springbootconcepts.beanValidators.validationGroups.OrderOnePost;
import com.example.springbootconcepts.beanValidators.validationGroups.OrderOnePut;
import com.example.springbootconcepts.validators.customValidators.ValidateEmployeeType;
import com.example.springbootconcepts.validators.customValidators.ValidateID;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    @ValidateID(groups = OrderOnePut.class)
    @Null(groups = OrderOnePost.class)
    private UUID id;

    @NotNull(message = "First Name should not be null",groups = OrderOnePost.class)
    private String firstName;

    @NotNull(message = "Joining Date should not be null",groups = OrderOnePost.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date joiningDate;

    @NotNull(message = "Last Name should not be null",groups = OrderOnePost.class)
    private String lastName;

    @Valid
    @NotNull(message = "Address should not be null",groups = OrderOnePost.class)
    private AddressDto address;

    @Email(groups = OrderOnePost.class,message = "Invalid email",regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotNull(message = "email should not be null",groups = OrderOnePost.class)
    private String email;

    @ValidateEmployeeType(groups = {OrderOnePost.class, OrderOnePut.class})
    private String employeeType;

}
