package com.example.external.consumer;

import org.apache.kafka.clients.CommonClientConfigs;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

public interface KafkaConsumerFailureHandler {

    /**
     * The retry template which will be used to handle failure when consume message. <br>
     * Please note when using {@link BackOffPolicy}, we must also configure the
     * {@link CommonClientConfigs#MAX_POLL_INTERVAL_MS_CONFIG} to be greater than total wait time in
     * {@link RetryTemplate} to ensure that the consumer not considered to be failed and need
     * to rebalance.
     *
     * @return {@link RetryTemplate}
     * @see CommonClientConfigs#MAX_POLL_INTERVAL_MS_DOC
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
     * Please note: <br>
     * 1. When using {@link BackOffPolicy}, we must also configure the
     * {@link CommonClientConfigs#MAX_POLL_INTERVAL_MS_CONFIG} to be greater than total wait time in
     * {@link RetryTemplate} to ensure that the consumer not considered to be failed and need
     * to rebalance. <br>
     * 2. {@link ErrorHandler} and {@link RetryTemplate} should not be used
     * together, we must choose either one. Please don't override this if we intend to use
     * {@link RetryTemplate}.
     *
     * @return {@link ErrorHandler}
     * @see <a href='https://www.linkedin.com/pulse/kafka-consumer-retry-rob-golder/'>Kafka Consumer Retry</a>
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
    default boolean hasErrorHandler() {
        return null != getErrorHandler();
    }
}
