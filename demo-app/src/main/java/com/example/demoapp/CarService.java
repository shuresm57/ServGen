package com.example.demoapp;

import org.example.autocrud.AutoCrudService;
import org.springframework.stereotype.Service;

@Service
@AutoCrudService(entity = Car.class, repository = CarRepository.class)
public class CarService extends BaseCarService {
    public CarService(CarRepository repository) {
        super(repository);
    }
}