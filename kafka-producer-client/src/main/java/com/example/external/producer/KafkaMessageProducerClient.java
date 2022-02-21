package com.example.external.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.RequiredArgsConstructor;

/**
 * The base implementation of {@link MessageProducerGateway} using Kafka. We use
 * {@link RoutingKafkaTemplate} rather than usual {@link KafkaTemplate} because we want to
 * give a flexible implementation of producer configuration on each topic.
 */
@Service
@RequiredArgsConstructor
public class KafkaMessageProducerClient implements MessageProducerGateway {

    private final RoutingKafkaTemplate routingKafkaTemplate;

    @Override
    public void send(MessageProducerCmd cmd) {
        ListenableFuture<SendResult<Object, Object>> sendResult = routingKafkaTemplate
            .send(cmd.getTopic(), cmd.getKey(), cmd.getPayload());

        sendResult.addCallback(new ListenableFutureCallback<SendResult<Object, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                cmd.getFailedCallback().accept(throwable);
            }

            @Override
            public void onSuccess(SendResult<Object, Object> result) {
                cmd.getSuccessCallback().run();
            }
        });
    }
}
