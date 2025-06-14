package com.example.springbootconcepts.dto;

import com.example.springbootconcepts.beanValidators.validationGroups.OrderOnePost;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    @NotNull(groups = OrderOnePost.class)
    private String street;
    @NotNull(groups = OrderOnePost.class)
    private String aptNum;
    @NotNull(groups = OrderOnePost.class)
    private String pinCode;
    @NotNull(groups = OrderOnePost.class)
    private String flatName;
}
