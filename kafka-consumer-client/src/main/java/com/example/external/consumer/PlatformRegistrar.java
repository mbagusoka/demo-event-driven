package com.example.external.consumer;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

/**
 * Credit to <a href="https://github.com/harvanir/springboot-starter-kafka">Harvan Irsyadi's github</a>
 */
public class PlatformRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(
        @NonNull AnnotationMetadata annotationMetadata,
        @NonNull BeanDefinitionRegistry beanDefinitionRegistry
    ) {
        registerConsumerContainer(beanDefinitionRegistry);
    }

    private void registerConsumerContainer(BeanDefinitionRegistry registry) {
        new KafkaConsumerContainerRegistrar().register(registry, environment);
    }

}
