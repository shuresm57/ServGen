package com.example.demoapp.service;

import com.example.demoapp.service.BaseCarService;
import com.example.demoapp.model.Car;
import com.example.demoapp.repository.CarRepository;
import org.example.autocrud.AutoCrudService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 *  Instead of having to write boilerplate code such as:
 *
 *<pre>{@code
 *     public Car findById(Long id){
 *          return carRepository.findById();
 *    }
 *
 * }</pre>
 *
 * We can now just annotate the service class and put entity and repository. The annotation then
 * creates a compiled abstract class, that we can extend to so we have the required methods.
 * If we need to write custom business logical methods, we can still add these to this service, such as the
 * <code>findAllRedCars</code> methods.
*/

@Service
@AutoCrudService(entity = Car.class, repository = CarRepository.class)
public class CarService extends BaseCarService {
    public CarService(CarRepository repository) {
        super(repository);
    }

    // Custom business logic also works.
    public List<Car> findAllRedCars() {
        return super.findAll().stream()
                .filter(car -> "red".equalsIgnoreCase(car.getColor()))
                .collect(Collectors.toList());
    }
}