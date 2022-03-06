package com.example.external.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
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

    /**
     * The error handler which can be used instead of using {@link RetryTemplate} and
     * {@link RecoveryCallback} combination. <br>
     * The typical use case for this method is when we want to use stateful retry using
     * {@link SeekToCurrentErrorHandler}. <br><br>
     * <p>
     * Please note that the {@link ErrorHandler} and {@link RetryTemplate} should not be used
     * together, we must choose. Please don't override this if we intend to use
     * {@link RetryTemplate}.
     *
     * @return {@link ErrorHandler}
     */
    default ErrorHandler getErrorHandler() {
        return null;
    }

    /**
     * Marks that this class using {@link ErrorHandler} rather than combination of
     * {@link RetryTemplate} and {@link RecoveryCallback} to handle failures.
     *
     * @return true if {@link ErrorHandler} is supplied.
     */
    default boolean isUseErrorHandler() {
        return null != getErrorHandler();
    }
}
