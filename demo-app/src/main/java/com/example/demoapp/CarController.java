package com.example.demoapp;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
