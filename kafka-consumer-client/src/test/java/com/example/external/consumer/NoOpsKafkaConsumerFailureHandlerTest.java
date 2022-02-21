package com.example.external.consumer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class NoOpsKafkaConsumerFailureHandlerTest {

    private final NoOpsKafkaConsumerFailureHandler handler = new NoOpsKafkaConsumerFailureHandler();

    @Test
    void givenRequest_whenGetRetryTemplate_shouldReturnCorrectResult() {
        assertNotNull(handler.getRetryTemplate());
    }

    @Test
    void givenRequest_whenGetRecoveryCallback_shouldReturnCorrectResult() {
        assertNotNull(handler.getRecoveryCallback());
    }

    @Test
    void givenRequest_whenGetContainerName_shouldReturnCorrectResult() {
        assertNotNull(handler.getContainerName());
    }
}
