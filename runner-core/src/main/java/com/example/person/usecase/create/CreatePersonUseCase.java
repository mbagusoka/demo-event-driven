package com.example.person.usecase.create;

import org.springframework.stereotype.Service;

import com.example.gateway.person.PersonGateway;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreatePersonUseCase implements CreatePerson {

    private final PersonGateway personGateway;

    @Override
    public void create(CreatePersonCmd cmd) {
        personGateway.save(cmd.toEntity());
    }
}
