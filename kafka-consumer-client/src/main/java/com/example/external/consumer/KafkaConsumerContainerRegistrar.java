package com.example.external.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * This class purpose is to register the {@link ConcurrentKafkaListenerContainerFactory} based on
 * value defined in {@link KafkaConsumerContainerContextHolder}.
 */
@Slf4j
public class KafkaConsumerContainerRegistrar {

    public void register(BeanDefinitionRegistry beanDefinitionRegistry, Environment environment) {
        KafkaConsumerContainerContextHolder propertiesMap = constructPropertiesMap(environment);

        registerFactory(propertiesMap, beanDefinitionRegistry);
    }

    private KafkaConsumerContainerContextHolder constructPropertiesMap(Environment environment) {
        Binder binder = Binder.get(environment);
        KafkaConsumerContainerContextHolder contextHolder = binder
            .bind(
                KafkaConsumerContainerContextHolder.PROPERTIES_PREFIX,
                Bindable.of(KafkaConsumerContainerContextHolder.class)
            )
            .orElseThrow(() -> new KafkaException("Consumer Properties not defined"));

        contextHolder.getConsumersContainer().forEach(containerProperties -> log.info(
            "Loading kafka scheduler: schedulerProperties: {}",
            containerProperties
        ));

        return contextHolder;
    }

    private Map<String, Object> consumerConfigs(
        KafkaConsumerContainerContextHolder.KafkaConsumerContainerProperty properties
    ) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, properties.getAutoOffsetReset());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getGroupId());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, properties.getMaxPollRecords());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, properties.getMaxPollInterval());

        return props;
    }

    private ConsumerFactory<String, String> consumerFactory(
        KafkaConsumerContainerContextHolder.KafkaConsumerContainerProperty properties
    ) {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs(properties));
    }

    private void registerDefault(
        KafkaConsumerContainerContextHolder propertiesFactory,
        BeanDefinitionRegistry registry
    ) {
        propertiesFactory.getConsumersContainer()
            .stream()
            .filter(properties -> "default".equals(properties.getContainerName()))
            .findAny()
            .ifPresent(defaultProperties -> registerBeanDefinition(
                registry,
                "kafkaListenerContainerFactory",
                defaultProperties
            ));

    }

    private GenericBeanDefinition createBeanDefinition(
        KafkaConsumerContainerContextHolder.KafkaConsumerContainerProperty properties
    ) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ConcurrentKafkaListenerContainerFactory.class);

        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();

        propertyValues.addPropertyValue("concurrency", properties.getConcurrency());
        propertyValues.addPropertyValue("consumerFactory", consumerFactory(properties));

        return beanDefinition;
    }

    private void registerBeanDefinition(
        BeanDefinitionRegistry registry,
        String beanName,
        KafkaConsumerContainerContextHolder.KafkaConsumerContainerProperty properties
    ) {
        log.info("Registering bean \"{}\" with properties: {}", beanName, properties);

        GenericBeanDefinition beanDefinition = createBeanDefinition(properties);
        registry.registerBeanDefinition(beanName, beanDefinition);
    }

    private void registerFactory(
        KafkaConsumerContainerContextHolder propertiesMap, BeanDefinitionRegistry registry
    ) {
        registerDefault(propertiesMap, registry);

        propertiesMap.getConsumersContainer()
            .forEach(properties ->
                registerBeanDefinition(registry, properties.getContainerName(), properties)
            );
    }

}
