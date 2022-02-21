package com.example.external.consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class KafkaConsumerFailureHandlerFactoryTest {

    @Mock
    private KafkaConsumerFailureHandler handlerDummy;

    private KafkaConsumerFailureHandlerFactory factory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenRequest_whenGet_shouldReturnCorrectResult() {
        when(handlerDummy.getContainerName()).thenReturn("dummy");
        factory = new KafkaConsumerFailureHandlerFactory(Collections.singletonList(handlerDummy));

        KafkaConsumerFailureHandler actual = factory.get("dummy");

        assertNotNull(actual);
    }

    @Test
    void givenRequestNotFound_whenGet_shouldReturnDefaultResult() {
        factory = new KafkaConsumerFailureHandlerFactory(
            Collections.singletonList(new NoOpsKafkaConsumerFailureHandler())
        );

        KafkaConsumerFailureHandler actual = factory.get("dummy");

        assertNotNull(actual);
        assertEquals("default", actual.getContainerName());
    }
}
