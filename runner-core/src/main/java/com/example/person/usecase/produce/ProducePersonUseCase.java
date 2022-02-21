package com.example.person.usecase.produce;

import org.springframework.stereotype.Service;

import com.example.common.transformer.StringJsonTransformer;
import com.example.external.producer.MessageProducerCmd;
import com.example.external.producer.MessageProducerGateway;
import com.example.person.properties.PersonTopicContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducePersonUseCase implements ProducePerson {

    private final MessageProducerGateway producerGateway;

    private final StringJsonTransformer transformer;

    private final PersonTopicContextHolder topicContextHolder;

    @Override
    public void produce(ProducePersonCmd cmd) {
        String payload = transformer.transform(cmd);
        MessageProducerCmd producerCmd = MessageProducerCmd
            .valueOf(topicContextHolder.getCreateTopic(), payload)
            .withSuccessCallback(() -> log.info("Success to produce event {}", payload))
            .withFailedCallback(e ->
                log.error("Fail to produce event {} with error {}", payload, e.getMessage())
            );

        producerGateway.send(producerCmd);
    }
}
