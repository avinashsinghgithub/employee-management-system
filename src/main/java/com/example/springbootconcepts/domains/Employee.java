package com.example.springbootconcepts.domains;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "first_name")
    @Size(min = 5, message = "{validation.name.size.too_short}")
    @Size(max = 10, message = "{validation.name.size.too_long}")
    String firstName;

    @Column(name = "joiningDate")
    Date joiningDate;

    @Column(name = "last_name")
    @Size(min = 5, message = "{validation.name.size.too_short}")
    @Size(max = 10, message = "{validation.name.size.too_long}")
    String lastName;

    @OneToOne(cascade = CascadeType.ALL)
    Address address;


    @ManyToOne
    @JoinColumn(name="department_id")
    Department department;

    @Column(name = "email")
    String email;

    @Column(name = "employeeType")
    String employeeType;
//    public Department getDepartment() {
//        return department;
//    }
//
//    public void setDepartment(Department department) {
//        this.department = department;
//    }

}
