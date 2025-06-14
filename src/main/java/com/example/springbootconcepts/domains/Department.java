package com.example.springbootconcepts.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    @Column(name = "departmentName")
    String departmentName;

    @Column(name = "description")
    String description;

    @OneToMany(mappedBy = "department",cascade = CascadeType.ALL)
    List<Employee> employees ;
}
