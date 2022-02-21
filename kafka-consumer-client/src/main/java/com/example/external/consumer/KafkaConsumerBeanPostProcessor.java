package com.example.external.consumer;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.lang.NonNull;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This class purpose is to intercept {@link ConcurrentKafkaListenerContainerFactory} bean
 * creation to add the failure handler mechanism specific to each bean.
 *
 * @see KafkaConsumerFailureHandler
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
@DependsOn({"kafkaConsumerFailureHandlerFactory"})
public class KafkaConsumerBeanPostProcessor implements BeanPostProcessor {

    private final KafkaConsumerFailureHandlerFactory kafkaConsumerFailureHandlerFactory;

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) {
        if (bean instanceof ConcurrentKafkaListenerContainerFactory) {
            updateContainerProperties(
                (ConcurrentKafkaListenerContainerFactory<?, ?>) bean,
                beanName
            );
        }

        return bean;
    }

    private void updateContainerProperties(
        ConcurrentKafkaListenerContainerFactory<?, ?> factory,
        String beanName
    ) {
        KafkaConsumerFailureHandler failureHandler = kafkaConsumerFailureHandlerFactory
            .get(beanName);

        factory.setRetryTemplate(failureHandler.getRetryTemplate());
        factory.setRecoveryCallback(failureHandler.getRecoveryCallback());
    }

}
