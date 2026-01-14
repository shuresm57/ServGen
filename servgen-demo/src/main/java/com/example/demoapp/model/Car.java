//=========================================================|
//  Copyright © Valdemar Støvring Storgaard, December 2025.|
//=========================================================|

package com.example.demoapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "color")
    private String color;
    
    @Column(name = "reg_no")
    private int regNo;

    public Car() {}

    public Car(Long id, String color, int regNo) {
        this.id = id;
        this.color = color;
        this.regNo = regNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getRegNo() {
        return regNo;
    }

    public void setRegNo(int regNo) {
        this.regNo = regNo;
    }
}
