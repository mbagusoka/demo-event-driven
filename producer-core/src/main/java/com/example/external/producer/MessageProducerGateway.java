package com.example.external.producer;

public interface MessageProducerGateway {

    /**
     * Send the message with it's related topic and callbacks.
     *
     * @param cmd The request holder class.
     */
    void send(MessageProducerCmd cmd);
}
