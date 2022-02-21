package com.example.external.consumer;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

/**
 * This class is the default implementation of {@link KafkaConsumerFailureHandler} if there is no
 * specific bean {@link ConcurrentKafkaListenerContainerFactory} behavior defined.
 */
@Component
public class NoOpsKafkaConsumerFailureHandler implements KafkaConsumerFailureHandler {

    private static final int NO_RETRY = 1;

    /**
     * No retry attempt.
     */
    @Override
    public RetryTemplate getRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(NO_RETRY));

        return retryTemplate;
    }

    /**
     * Do nothing
     */
    @Override
    public RecoveryCallback<Void> getRecoveryCallback() {
        return context -> null;
    }

    @Override
    public String getContainerName() {
        return "default";
    }
}
