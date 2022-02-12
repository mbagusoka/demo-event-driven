package com.example.person.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.person.usecase.create.CreatePerson;
import com.example.person.usecase.create.CreatePersonCmd;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
public class PersonRestController {

    private final CreatePerson createPerson;

    @PostMapping
    public void create(@Valid @RequestBody CreatePersonCmd cmd) {
        createPerson.create(cmd);
    }
}
