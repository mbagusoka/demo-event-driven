package com.example.external.producer;

import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaMessageProducerClient implements MessageProducerGateway {

    private final RoutingKafkaTemplate routingKafkaTemplate;

    @Override
    public void send(MessageProducerCmd cmd) {
        ListenableFuture<SendResult<Object, Object>> sendResult = routingKafkaTemplate
            .send(cmd.getTopic(), cmd.getPayload());

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
