//=========================================================|
//  Copyright © Valdemar Støvring Storgaard, December 2025.|
//=========================================================|

package com.example.demoapp.integration;

import com.example.demoapp.model.Car;
import com.example.demoapp.repository.CarRepository;
import com.example.demoapp.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is an integration test, proving that it works with the JPA interface methods.
 *
 */
@DataJpaTest
@Import(CarService.class)
@ActiveProfiles("test")
public class CarServiceJpaIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarService carService;

    @Test
    void shouldPerformCrudOperationsViaGeneratedBaseMethods() {
        // Given: A car entity
        Car newCar = new Car(null, "Blue", 12345);

        // When: Save via generated method
        Car savedCar = carService.save(newCar);
        entityManager.flush();

        // Then: Car should be saved with ID
        assertNotNull(savedCar.getId());
        assertEquals("Blue", savedCar.getColor());
        assertEquals(12345, savedCar.getRegNo());

        // When: Find by ID via generated method
        Car foundCar = carService.findById(savedCar.getId());

        // Then: Should find the car
        assertNotNull(foundCar);
        assertEquals(savedCar.getId(), foundCar.getId());
        assertEquals("Blue", foundCar.getColor());

        // When: Check existence via generated method
        boolean exists = carService.existsById(savedCar.getId());

        // Then: Should exist
        assertTrue(exists);

        // When: Find all via generated method
        List<Car> allCars = carService.findAll();

        // Then: Should contain our car
        assertEquals(1, allCars.size());
        assertEquals(savedCar.getId(), allCars.get(0).getId());

        // When: Delete via generated method
        carService.deleteById(savedCar.getId());
        entityManager.flush();

        // Then: Should be deleted
        assertFalse(carService.existsById(savedCar.getId()));
        assertNull(carService.findById(savedCar.getId()));
    }

    @Test
    void shouldUseCustomBusinessMethodsAlongsideGenerated() {
        // Given: Some cars with different colors
        Car redCar1 = carService.save(new Car(null, "red", 11111));
        Car redCar2 = carService.save(new Car(null, "Red", 22222));
        Car blueCar = carService.save(new Car(null, "blue", 33333));
        entityManager.flush();

        // When: Use custom business method (findAllRedCars)
        List<Car> redCars = carService.findAllRedCars();

        // Then: Should find only red cars (case-insensitive)
        assertEquals(2, redCars.size());
        assertTrue(redCars.stream().allMatch(car -> 
            "red".equalsIgnoreCase(car.getColor())));

        // When: Use generated method for all cars
        List<Car> allCars = carService.findAll();

        // Then: Should find all cars
        assertEquals(3, allCars.size());
    }
}