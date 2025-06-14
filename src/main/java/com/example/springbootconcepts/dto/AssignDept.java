package com.example.springbootconcepts.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AssignDept {
    UUID id;
    List<UUID> employeesIDs;
}
