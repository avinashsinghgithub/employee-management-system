package com.example.springbootconcepts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Record {
    private String recordId;
    private String recordName;
    private String recordDescription;

}
