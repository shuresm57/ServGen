package com.example.demoapp.controller;

import com.example.demoapp.model.Car;
import com.example.demoapp.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class CarController {

    private final CarService carService;

    @GetMapping("/")
    public List<Car> findAllCars(){
        return carService.findAll();
    }

}
