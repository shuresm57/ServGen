//=========================================================|
//  Copyright © Valdemar Støvring Storgaard, December 2025.|
//=========================================================|

package io.servgen.demo.service;

import io.servgen.demo.model.Car;
import io.servgen.demo.model.Person;
import io.servgen.demo.repository.CarRepository;
import io.servgen.demo.repository.PersonRepository;
import io.servgen.annotation.ServGen;
import org.springframework.stereotype.Service;

/**
 * This is just a proof of concept class - go to CarService for explanation of the <code>AutoCrud</code> annotation.
 *
 * @see CarService
 */


@Service
@ServGen(entity = Person.class, repository = PersonRepository.class)
public class PersonService extends BasePersonService{
    public PersonService(PersonRepository repository){
        super(repository);
    }
}