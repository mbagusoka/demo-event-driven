package com.example.person.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.person.usecase.create.CreatePerson;
import com.example.person.usecase.create.CreatePersonCmd;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreatePersonConsumer {

    private final ObjectMapper mapper;

    private final CreatePerson createPerson;

    @KafkaListener(topics = "${person.create-topic}", containerFactory = "create-person")
    @SneakyThrows(JsonProcessingException.class)
    public void listen(ConsumerRecord<String, String> message) {
        log.info("Processing message: {}", message);

        CreatePersonCmd cmd = mapper.readValue(message.value(), CreatePersonCmd.class);

        createPerson.create(cmd);
    }
}
