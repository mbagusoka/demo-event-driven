package com.example.external.producer;

import static com.example.external.producer.KafkaProducerProperties.TopicProducerProperty;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.util.Assert;

@Configuration
public class KafkaProducerConfiguration {

    @Bean
    public RoutingKafkaTemplate routingKafkaTemplate(KafkaProducerProperties properties) {
        Assert.notEmpty(
            properties.getTopicProducers(),
            () -> "Producer Properties cannot be empty"
        );

        Map<Pattern, ProducerFactory<Object, Object>> producerMap = new LinkedHashMap<>();
        properties.getTopicProducers()
            .forEach(property -> createProducer(producerMap, property));

        return new RoutingKafkaTemplate(producerMap);
    }

    private void createProducer(
        Map<Pattern, ProducerFactory<Object, Object>> producerMap,
        TopicProducerProperty topicProducerProperty
    ) {
        DefaultKafkaProducerFactory<Object, Object> producerFactory =
            getKafkaProducerFactory(topicProducerProperty);

        Pattern topicPattern = Pattern.compile(topicProducerProperty.getTopicPrefix().concat(".*"));
        producerMap.put(topicPattern, producerFactory);
    }

    private DefaultKafkaProducerFactory<Object, Object> getKafkaProducerFactory(
        TopicProducerProperty topicProducerProperty
    ) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            topicProducerProperty.getBootstrapServers()
        );
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, topicProducerProperty.getAck());

        return new DefaultKafkaProducerFactory<>(properties);
    }
}
