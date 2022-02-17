package com.example.external.producer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaProducerProperties {

    private Map<String, TopicProducerProperty> topicProducers = new HashMap<>();

    @Data
    public static class TopicProducerProperty {

        private String bootstrapServers;

        private String ack;
    }
}
