package com.example.person.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.person.model.Person;
import lombok.Data;

@Entity
@Table(name = "person")
@Data
public class PersonEntity implements Person {

    private static final long serialVersionUID = -8314411517795636670L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public static PersonEntity valueOf(Person person) {
        PersonEntity entity = new PersonEntity();
        entity.setName(person.getName());

        return entity;
    }
}
