package com.example.gateway.person;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.person.jpa.PersonEntity;
import com.example.person.jpa.PersonRepository;
import com.example.person.model.Person;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JpaPersonGateway implements PersonGateway {

    private final PersonRepository personRepository;

    @Override
    public void save(Person person) {
        personRepository.save(PersonEntity.valueOf(person));
    }

    @Override
    public Optional<Person> findById(Long id) {
        return personRepository.findById(id)
            .map(Person.class::cast);
    }
}
