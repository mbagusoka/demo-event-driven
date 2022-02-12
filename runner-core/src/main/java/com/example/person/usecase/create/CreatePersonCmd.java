package com.example.person.usecase.create;

import javax.validation.constraints.NotBlank;

import com.example.person.model.Person;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreatePersonCmd {

    @NotBlank(message = "Name cannot be empty")
    String name;

    public Person toEntity() {
        return new Person() {
            @Override
            public Long getId() {
                return null;
            }

            @Override
            public String getName() {
                return name;
            }
        };
    }
}
