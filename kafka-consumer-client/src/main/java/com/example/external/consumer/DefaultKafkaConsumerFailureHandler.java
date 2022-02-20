package com.example.external.consumer;

import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.LoggingErrorHandler;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class DefaultKafkaConsumerFailureHandler implements KafkaConsumerFailureHandler {

    private static final int NO_RETRY = 1;

    @Override
    public RetryTemplate getRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(NO_RETRY));

        return retryTemplate;
    }

    @Override
    public ErrorHandler getErrorHandler() {
        return new LoggingErrorHandler();
    }

    @Override
    public RecoveryCallback<Void> getRecoveryCallback() {
        return context -> null;
    }

    @Override
    public String getContainerName() {
        return "default";
    }
}
