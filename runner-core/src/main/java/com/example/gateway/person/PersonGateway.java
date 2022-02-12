package com.example.gateway.person;

import java.util.Optional;

import com.example.person.model.Person;

public interface PersonGateway {

    void save(Person person);

    Optional<Person> findById(Long id);
}
