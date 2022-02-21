package com.example.external.producer;

import java.util.function.Consumer;

import lombok.Value;
import lombok.With;

@Value
public class MessageProducerCmd {

    /**
     * Topic or channel we want to send message.
     */
    String topic;

    /**
     * The key of message we want to provide. This is optional. <br>
     * For Kafka implementation, this is required if we want to ensure same message produced in
     * same partition.
     */
    String key;

    /**
     * JSON String of the object we want to send as message.
     */
    String payload;

    /**
     * Callback to be called when message failed to be sent.
     */
    @With
    Consumer<Throwable> failedCallback;

    /**
     * Callback to be called when message success to be sent.
     */
    @With
    Runnable successCallback;

    public static MessageProducerCmd valueOf(String topic, String payload) {
        return new MessageProducerCmd(topic, null, payload, t -> {}, () -> {});
    }
}
