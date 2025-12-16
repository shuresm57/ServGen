package com.example.demoapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CarServiceTest {
    
    @Autowired
    private CarService carService;
    
    @MockitoBean
    private CarRepository carRepository;
    
    @Test
    public void testFindAll() {
        // Arrange
        Car car1 = new Car(1L, "Red", 12345);
        Car car2 = new Car(2L, "Blue", 67890);
        List<Car> expectedCars = Arrays.asList(car1, car2);
        
        when(carRepository.findAll()).thenReturn(expectedCars);
        
        // Act
        List<Car> actualCars = carService.findAll();
        
        // Assert
        assertEquals(expectedCars, actualCars);
        verify(carRepository).findAll();
    }
    
    @Test
    public void testFindById() {
        // Arrange
        Car expectedCar = new Car(1L, "Green", 11111);
        when(carRepository.findById(1L)).thenReturn(Optional.of(expectedCar));
        
        // Act
        Car actualCar = carService.findById(1L);
        
        // Assert
        assertEquals(expectedCar, actualCar);
        verify(carRepository).findById(1L);
    }
    
    @Test
    public void testSave() {
        // Arrange
        Car carToSave = new Car(null, "Yellow", 99999);
        Car savedCar = new Car(3L, "Yellow", 99999);
        when(carRepository.save(carToSave)).thenReturn(savedCar);
        
        // Act
        Car result = carService.save(carToSave);
        
        // Assert
        assertEquals(savedCar, result);
        verify(carRepository).save(carToSave);
    }
    
    @Test
    public void testExistsById() {
        // Arrange
        when(carRepository.existsById(1L)).thenReturn(true);
        
        // Act
        boolean exists = carService.existsById(1L);
        
        // Assert
        assertTrue(exists);
        verify(carRepository).existsById(1L);
    }
    
    @Test
    public void testDeleteById() {
        // Act
        carService.deleteById(1L);
        
        // Assert
        verify(carRepository).deleteById(1L);
    }
}