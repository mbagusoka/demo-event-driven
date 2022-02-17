package com.example.external.producer;

import java.util.function.Consumer;

import lombok.Value;

@Value
public class MessageProducerCmd {

    String topic;

    String key;

    String payload;

    Consumer<Throwable> failedCallback;

    Runnable successCallback;

    public static MessageProducerCmd valueOf(String topic, String payload) {
        return new MessageProducerCmd(topic, null, payload, t -> {}, () -> {});
    }
}
