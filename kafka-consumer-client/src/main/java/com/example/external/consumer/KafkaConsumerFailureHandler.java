package com.example.external.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.support.RetryTemplate;

public interface KafkaConsumerFailureHandler {

    /**
     * The retry template which will be used to handle failure when consume message.
     *
     * @return {@link RetryTemplate}
     */
    RetryTemplate getRetryTemplate();

    /**
     * The callback function which will be called when the retry is exhausted. This function
     * should return null since the function which handles {@link KafkaListener} is return void.
     *
     * @return {@link RecoveryCallback<Void>}
     */
    RecoveryCallback<Void> getRecoveryCallback();

    /**
     * The name of the {@link ConcurrentKafkaListenerContainerFactory} bean which declared as a key
     * in {@link KafkaConsumerContainerContextHolder#getConsumersContainer()}.
     *
     * @return name of the bean which this class is belonged to.
     */
    String getContainerName();
}
