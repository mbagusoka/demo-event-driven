package com.example.external.producer;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.ProducerFactory;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "kafka.producer")
@Data
public class KafkaProducerProperties {

    private List<TopicProducerProperty> topicProducers = new ArrayList<>();

    @Data
    public static class TopicProducerProperty {

        /**
         * The topic prefix to match between the topic and the related {@link ProducerFactory}.
         */
        private String topicPrefix;

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
