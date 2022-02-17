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
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.util.Assert;

@Configuration
public class KafkaProducerConfiguration {

    @Bean
    public RoutingKafkaTemplate routingKafkaTemplate(
        KafkaProducerProperties properties,
        GenericApplicationContext context
    ) {
        Assert.notEmpty(properties.getTopicProducers(), () -> "Producer Map cannot be empty");

        Map<Pattern, ProducerFactory<Object, Object>> producerMap = new LinkedHashMap<>();
        properties.getTopicProducers().forEach(
            (key, topicProducerProperty) -> createProducer(context, key, topicProducerProperty));

        return new RoutingKafkaTemplate(producerMap);
    }

    private void createProducer(
        GenericApplicationContext context,
        String key,
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

        DefaultKafkaProducerFactory<Object, Object> producerFactory =
            new DefaultKafkaProducerFactory<>(properties);

        context.registerBean(DefaultKafkaProducerFactory.class, key, producerFactory);
    }
}
