package com.example.demoapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    private CarService carService;

    @BeforeEach
    void setUp() {
        carService = new CarService(carRepository);
    }

    @Test
    void testFindById() {
        // Given
        Long carId = 1L;
        Car expectedCar = new Car(carId, "Red", 12345);
        when(carRepository.findById(carId)).thenReturn(Optional.of(expectedCar));

        // When
        Car actualCar = carService.findById(carId);

        // Then
        assertNotNull(actualCar);
        assertEquals(expectedCar.getId(), actualCar.getId());
        assertEquals(expectedCar.getColor(), actualCar.getColor());
        assertEquals(expectedCar.getRegNo(), actualCar.getRegNo());
        verify(carRepository).findById(carId);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        Long carId = 999L;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        // When
        Car actualCar = carService.findById(carId);

        // Then
        assertNull(actualCar);
        verify(carRepository).findById(carId);
    }

    @Test
    void testFindAll() {
        // Given
        List<Car> expectedCars = Arrays.asList(
            new Car(1L, "Red", 11111),
            new Car(2L, "Blue", 22222)
        );
        when(carRepository.findAll()).thenReturn(expectedCars);

        // When
        List<Car> actualCars = carService.findAll();

        // Then
        assertNotNull(actualCars);
        assertEquals(2, actualCars.size());
        assertEquals(expectedCars, actualCars);
        verify(carRepository).findAll();
    }

    @Test
    void testSave() {
        // Given
        Car carToSave = new Car(null, "Green", 33333);
        Car savedCar = new Car(1L, "Green", 33333);
        when(carRepository.save(carToSave)).thenReturn(savedCar);

        // When
        Car actualCar = carService.save(carToSave);

        // Then
        assertNotNull(actualCar);
        assertEquals(savedCar.getId(), actualCar.getId());
        assertEquals(savedCar.getColor(), actualCar.getColor());
        assertEquals(savedCar.getRegNo(), actualCar.getRegNo());
        verify(carRepository).save(carToSave);
    }

    @Test
    void testExistsById() {
        // Given
        Long carId = 1L;
        when(carRepository.existsById(carId)).thenReturn(true);

        // When
        boolean exists = carService.existsById(carId);

        // Then
        assertTrue(exists);
        verify(carRepository).existsById(carId);
    }

    @Test
    void testExistsByIdNotFound() {
        // Given
        Long carId = 999L;
        when(carRepository.existsById(carId)).thenReturn(false);

        // When
        boolean exists = carService.existsById(carId);

        // Then
        assertFalse(exists);
        verify(carRepository).existsById(carId);
    }

    @Test
    void testDeleteById() {
        // Given
        Long carId = 1L;

        // When
        carService.deleteById(carId);

        // Then
        verify(carRepository).deleteById(carId);
    }
}