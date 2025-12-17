//=========================================================|
//  Copyright © Valdemar Støvring Storgaard, December 2025.|
//=========================================================|

package com.example.demoapp.service;

import com.example.demoapp.model.Person;
import com.example.demoapp.repository.PersonRepository;
import io.servgen.annotation.ServGen;
import org.springframework.stereotype.Service;

/**
 * This is just a proof of concept class - go to CarService for explanation of the <code>AutoCrud</code> annotation.
 *
 * @see com.example.demoapp.service.CarService;
 */


@Service
@ServGen(entity = Person.class, repository = PersonRepository.class)
public class PersonService extends BasePersonService{
    public PersonService(PersonRepository repository){
        super(repository);
    }
}