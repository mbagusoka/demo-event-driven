package com.example.external.consumer;

import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.support.RetryTemplate;

public interface KafkaConsumerFailureHandler {

    RetryTemplate getRetryTemplate();

    ErrorHandler getErrorHandler();

    RecoveryCallback<Void> getRecoveryCallback();

    String getContainerName();
}
