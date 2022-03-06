package com.example.person.consumer;

import java.util.function.BiConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.listener.adapter.RetryingMessageListenerAdapter;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.backoff.FixedBackOff;

import com.example.external.consumer.KafkaConsumerFailureHandler;
import com.example.external.producer.MessageProducerCmd;
import com.example.external.producer.MessageProducerGateway;
import com.example.person.properties.PersonTopicContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreatePersonConsumerFailureHandler implements KafkaConsumerFailureHandler {

    private final PersonTopicContextHolder personTopicContextHolder;

    private final MessageProducerGateway messageProducerGateway;

    @Override
    public RetryTemplate getRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(1));

        return retryTemplate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecoveryCallback<Void> getRecoveryCallback() {
        return context -> {
            ConsumerRecord<String, String> rec =
                (ConsumerRecord<String, String>) context
                    .getAttribute(RetryingMessageListenerAdapter.CONTEXT_RECORD);

            log.info("Recovery Create Topic with record {}", rec);

            MessageProducerCmd cmd = MessageProducerCmd
                .valueOf(personTopicContextHolder.getCreateTopicRetry(), rec.value());
            messageProducerGateway.send(cmd);

            return null;
        };
    }

    @Override
    public String getContainerName() {
        return personTopicContextHolder.getCreateTopic();
    }

    @Override
    public ErrorHandler getErrorHandler() {
        BiConsumer<ConsumerRecord<?, ?>, Exception> recoverer = (rec, e) -> {
            log.info("Error Handler Create Topic with record {}", rec);

            MessageProducerCmd cmd = MessageProducerCmd
                .valueOf(personTopicContextHolder.getCreateTopicRetry(),
                    String.valueOf(rec.value())
                );
            messageProducerGateway.send(cmd);
        };

        return new SeekToCurrentErrorHandler(recoverer, new FixedBackOff(20000L, 2L));
    }
}
