package com.example.external.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerFailureHandlerFactory {

    private final Map<String, KafkaConsumerFailureHandler> handlerMap = new HashMap<>();

    public KafkaConsumerFailureHandlerFactory(List<KafkaConsumerFailureHandler> handlers) {
        handlers.forEach(handler -> handlerMap.put(handler.getContainerName(), handler));
    }

    public KafkaConsumerFailureHandler get(String containerName) {
        return Optional.ofNullable(handlerMap.get(containerName))
            .orElseGet(() -> handlerMap.get("default"));
    }
}
