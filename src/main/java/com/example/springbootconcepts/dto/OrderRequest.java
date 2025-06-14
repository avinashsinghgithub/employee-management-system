package com.example.springbootconcepts.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderRequest {
    private UUID id;
    private String description;
}
