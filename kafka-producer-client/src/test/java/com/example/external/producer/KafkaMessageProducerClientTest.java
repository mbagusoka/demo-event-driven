package com.example.external.producer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

class KafkaMessageProducerClientTest {

    @InjectMocks
    private KafkaMessageProducerClient client;

    @Mock
    private RoutingKafkaTemplate routingKafkaTemplate;

    @Mock
    private ListenableFuture<SendResult<Object, Object>> future;

    @Mock
    private SendResult<Object, Object> sendResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void givenRequest_whenSend_shouldCallRoutingKafkaTemplate() {
        stubFuture();

        MessageProducerCmd cmd = new MessageProducerCmd(
            "topic",
            "key",
            "payload",
            e -> {
            },
            () -> {
            }
        );
        client.send(cmd);

        verify(routingKafkaTemplate).send(cmd.getTopic(), cmd.getKey(), cmd.getPayload());
    }

    @Test
    void givenSuccess_whenSend_shouldRunCallback() {
        stubFuture();
        doAnswer(this::stubSuccessInvocation).when(future).addCallback(
            ArgumentMatchers.<ListenableFutureCallback<SendResult<Object, Object>>>any()
        );

        AtomicBoolean successCalled = new AtomicBoolean(false);
        MessageProducerCmd cmd = new MessageProducerCmd(
            "topic",
            "key",
            "payload",
            e -> {
            },
            () -> successCalled.set(true)
        );
        client.send(cmd);

        assertTrue(successCalled.get());
    }

    @Test
    void givenFailure_whenSend_shouldRunCallback() {
        stubFuture();
        doAnswer(this::stubFailedInvocation).when(future).addCallback(
            ArgumentMatchers.<ListenableFutureCallback<SendResult<Object, Object>>>any()
        );

        AtomicBoolean failedCalled = new AtomicBoolean(false);
        MessageProducerCmd cmd = new MessageProducerCmd(
            "topic",
            "key",
            "payload",
            e -> failedCalled.set(true),
            () -> {
            }
        );
        client.send(cmd);

        assertTrue(failedCalled.get());
    }

    private void stubFuture() {
        when(routingKafkaTemplate.send(anyString(), anyString(), anyString()))
            .thenReturn(future);
    }

    private Object stubSuccessInvocation(InvocationOnMock invocation) {
        ListenableFutureCallback<SendResult<Object, Object>> callback =
            invocation.getArgument(0);
        callback.onSuccess(sendResult);

        return null;
    }

    private Object stubFailedInvocation(InvocationOnMock invocation) {
        ListenableFutureCallback<SendResult<Object, Object>> callback =
            invocation.getArgument(0);
        callback.onFailure(new Exception());

        return null;
    }
}
