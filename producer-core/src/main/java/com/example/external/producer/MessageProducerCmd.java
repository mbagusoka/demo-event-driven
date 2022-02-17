package com.example.external.producer;

import java.util.function.Consumer;

import lombok.Value;

@Value
public class MessageProducerCmd {

    String topic;

    String payload;

    Consumer<Throwable> failedCallback;

    Consumer<MessageMetadataAware> successCallback;

    public static MessageProducerCmd valueOf(String topic, String payload) {
        return new MessageProducerCmd(topic, payload, t -> {}, metadata -> {});
    }

    public static MessageProducerCmd valueOf(
        String topic,
        String payload,
        Consumer<Throwable> failedCallback,
        Consumer<MessageMetadataAware> successCallback
    ) {
        return new MessageProducerCmd(topic, payload, failedCallback, successCallback);
    }
}
