package com.example.springbootconcepts.domains;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;


    String street;
    @Column(name = "apt_name")
    String aptNum;
    @Column(name = "pin_code")
    String pinCode;
    @Column(name = "flatName")
    String flatName;


}
