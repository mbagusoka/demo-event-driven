package com.example.external.consumer;

import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.support.RetryTemplate;

public interface KafkaConsumerFailureHandler {

    RetryTemplate getRetryTemplate();

    RecoveryCallback<Void> getRecoveryCallback();

    String getContainerName();
}
