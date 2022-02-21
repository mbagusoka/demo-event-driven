package com.example.external.producer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "kafka.producer")
@Data
public class KafkaProducerProperties {

    private Map<String, TopicProducerProperty> topicProducers = new HashMap<>();

    @Data
    public static class TopicProducerProperty {

        /**
         * List of Kafka's clusters hostname with port comma separated.
         */
        private String bootstrapServers;

        /**
         * Kafka Ack mode, please refer to {@link ProducerConfig#ACKS_CONFIG}
         */
        private String ack;
    }
}
