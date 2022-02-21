package com.example.external.consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.support.RetryTemplate;

class KafkaConsumerBeanPostProcessorTest {

    @InjectMocks
    private KafkaConsumerBeanPostProcessor postProcessor;

    @Mock
    private KafkaConsumerFailureHandlerFactory handlerFactory;

    @Mock
    private ConcurrentKafkaListenerContainerFactory<?, ?> containerFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenKafkaContainerFactory_whenPostProcess_shouldCallHandlerFactory() {
        stubHandlerFactory();

        postProcessor.postProcessBeforeInitialization(containerFactory, "container");

        verify(handlerFactory).get("container");
    }

    @Test
    void givenKafkaContainerFactory_whenPostProcess_shouldUpdateFailureHandler() {
        stubHandlerFactory();

        postProcessor.postProcessBeforeInitialization(containerFactory, "container");

        verify(containerFactory).setRetryTemplate(any(RetryTemplate.class));
        verify(containerFactory)
            .setRecoveryCallback(ArgumentMatchers.<RecoveryCallback<Void>>any());
    }

    private void stubHandlerFactory() {
        when(handlerFactory.get(anyString())).thenReturn(new NoOpsKafkaConsumerFailureHandler());
    }
}
