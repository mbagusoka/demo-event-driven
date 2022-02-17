package com.example.external.producer;

public interface MessageProducerGateway {

    void send(MessageProducerCmd cmd);
}
