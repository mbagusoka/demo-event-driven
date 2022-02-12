package com.example.gateway.person;

import com.example.person.model.Person;

public interface PersonGateway {

    void save(Person person);

    Person findById(Long id);
}
