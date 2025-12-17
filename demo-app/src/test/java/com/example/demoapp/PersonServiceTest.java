package com.example.demoapp;

import com.example.demoapp.model.Person;
import com.example.demoapp.repository.PersonRepository;
import com.example.demoapp.service.PersonService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    private static Long personId;
    private static Person expectedPerson;

    @InjectMocks
    private PersonService personService;

    @BeforeAll
    static void setUpPerson(){
        personId = 1L;
        expectedPerson = new Person(personId, "Niko", 12);
    }

    @Test
    void testFindById() {
        // Given
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

    @Test
    void testFindByIdNotFound() {
        // Given
        when(personRepository.findById(personId)).thenReturn(Optional.empty());

        // When
        expectedPerson = personService.findById(personId);

        // Then
        assertNull(expectedPerson);
        verify(personRepository).findById(personId);
    }

    @Test
    void testFindAll() {
        // Given
        List<Person> expectedPersons = Arrays.asList(
                new Person(1L, "Niko", 13),
                new Person(2L, "Kristian", 22)
        );
        when(personRepository.findAll()).thenReturn(expectedPersons);

        // When
        List<Person> actualPersons = personService.findAll();

        // Then
        assertNotNull(actualPersons);
        assertEquals(2, actualPersons.size());
        assertEquals(expectedPersons, actualPersons);
        verify(personRepository).findAll();
    }

    @Test
    void testSave() {
        //Given
        Person personToSave = new Person(null, "Hayden", 20);
        Person savedPerson = new Person(1L, "Hayden", 20);
        when(personRepository.save(personToSave)).thenReturn(savedPerson);

        //When
        Person actualPerson = personService.save(personToSave);

        //Then
        assertNotNull(actualPerson);
        assertEquals(savedPerson.getId(), actualPerson.getId());
        assertEquals(savedPerson.getName(), actualPerson.getName());
        assertEquals(savedPerson.getAge(), actualPerson.getAge());
        verify(personRepository).save(personToSave);
    }

    @Test
    void testExistsById() {
        // Given
        when(personRepository.existsById(personId)).thenReturn(true);

        // When
        boolean exists = personService.existsById(personId);

        // Then
        assertTrue(exists);
        verify(personRepository).existsById(personId);
    }

    @Test
    void testExistsByIdNotFound() {
        // Given
        when(personRepository.existsById(personId)).thenReturn(false);

        // When
        boolean exists = personService.existsById(personId);

        // Then
        assertFalse(exists);
        verify(personRepository).existsById(personId);
    }

    @Test
    void testDeleteById() {
        // When
        personService.deleteById(personId);

        // Then
        verify(personRepository).deleteById(personId);
    }
}
