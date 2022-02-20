package com.example.person.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.person.usecase.produce.ProducePerson;
import com.example.person.usecase.produce.ProducePersonCmd;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
public class PersonRestController {

    private final ProducePerson producePerson;

    @PostMapping
    public void create(@Valid @RequestBody ProducePersonCmd cmd) {
        producePerson.produce(cmd);
    }
}
