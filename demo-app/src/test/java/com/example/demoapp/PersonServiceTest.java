package com.example.demoapp;

import com.example.demoapp.model.Car;
import com.example.demoapp.model.Person;
import com.example.demoapp.repository.PersonRepository;
import com.example.demoapp.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    private PersonService personService;

    @BeforeEach
    void setUp() {
        personService = new PersonService(personRepository);
    }

    @Test
    void testFindById() {
        // Given
        Long personId = 1L;
        Person expectedPerson = new Person(personId, "Niko", 12);
        when(personRepository.findById(personId)).thenReturn(Optional.of(expectedPerson));

        // When
        Person actualPerson = personService.findById(personId);

        // Then
        assertNotNull(actualPerson);
        assertEquals(expectedPerson.getId(), actualPerson.getId());
        assertEquals(expectedPerson.getName(), actualPerson.getName());
        assertEquals(expectedPerson.getAge(), actualPerson.getAge());
        verify(personRepository).findById(personId);
    }
}
