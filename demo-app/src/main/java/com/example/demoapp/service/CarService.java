package com.example.demoapp.service;

import com.example.demoapp.service.BaseCarService;
import com.example.demoapp.model.Car;
import com.example.demoapp.repository.CarRepository;
import org.example.autocrud.AutoCrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AutoCrudService(entity = Car.class, repository = CarRepository.class)
public class CarService extends BaseCarService {
    public CarService(CarRepository repository) {
        super(repository);
    }

    public List<Car> findAllRedCars() {
        return super.findAll().stream()
                .filter(car -> "red".equalsIgnoreCase(car.getColor()))
                .collect(Collectors.toList());
    }
}