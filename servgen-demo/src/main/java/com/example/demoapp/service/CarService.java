//=========================================================|
//  Copyright © Valdemar Støvring Storgaard, December 2025.|
//=========================================================|

package io.servgen.demo.service;

import io.servgen.demo.service.BaseCarService;
import io.servgen.demo.model.Car;
import io.servgen.demo.repository.CarRepository;
import io.servgen.annotation.ServGen;
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
 * We can now just annotate the service class with @ServGen and specify entity and repository. The annotation then
 * creates a compiled abstract class, that we can extend to so we have the required methods.
 * If we need to write custom business logical methods, we can still add these to this service, such as the
 * <code>findAllRedCars</code> methods.
 * <p>
 * To enable the generated CRUD functionality, the concrete service class
 * must define a constructor that accepts the repository and forwards it
 * to the generated base service constructor.
 *
 * <p>This constructor-based delegation allows Spring to inject the repository
 * while keeping all common CRUD behavior centralized in the generated base
 * class, leaving this service focused solely on application-specific logic.
 *<p>
 *<p>
 *A minimal price for avoiding 15+ lines of boilerplate code for simple CRUD methods.
 *
 *
 */

@Service
@ServGen(entity = Car.class, repository = CarRepository.class)
public class CarService extends BaseCarService {
    // Constructor required: Spring injects CarRepository → passes to BaseCarService constructor → enables generated CRUD methods
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