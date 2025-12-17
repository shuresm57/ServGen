package com.example.demoapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is just a proof of concept class - go to @CarService for explanation of the <code>AutoCrud</code> annotation.
 */

@Entity
@Table(name = "persons")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "age")
    private int age;

}
