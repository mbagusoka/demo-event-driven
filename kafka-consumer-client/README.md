# Kafka Consumer Client
This library provides mechanism for creating Kafka consumer configurations
automatically for each topic based on [ConcurrentKafkaListenerContainerFactory](org/springframework/kafka/config/ConcurrentKafkaListenerContainerFactory.java).

Reference: [What is consumer / listener container](https://stackoverflow.com/questions/64012396/what-is-a-listener-container-in-spring-for-apache-kafka)

## How to Use
* Add your defined topic consumer container configuration in application.yml.
```yml
kafka:
  consumer:
    consumers-container:
      - container-name: create-person # name of the container consumer beans.
        bootstrap-servers: localhost:9092 # comma separated hostname and port of Kafka broker.
        group-id: groupId # ID of the consumer group.
        concurrency: 3 # number of threads to serve the topic. Ideally match the number of topic's partitions.
        auto-offset-reset: latest # offset to fetch when the consumer join the topic for the first time.
        max-poll-records: 500 # number of records to be polled in one batch.
        max-poll-interval: 90000 # interval to wait for the consumer to be considered failed to consume message, in milliseconds.
      - container-name: create-person-retry
        bootstrap-servers: localhost:9092
        group-id: groupId
        concurrency: 3
        auto-offset-reset: latest
        max-poll-records: 500
        max-poll-interval: 60000
```
* Create your [@KafkaListener](org/springframework/kafka/annotation/KafkaListener.java) with container factory based on the name of consumer bean in application.yml.
```java
package com.example.person.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CreatePersonConsumer {

    @KafkaListener(topics = "${person.create-topic}", containerFactory = "create-person") 
    public void listen(ConsumerRecord<String, String> message) {
        log.info("Processing message: {}", message);
        
        // do some business logic
    }
    
}
```
* If you want to add failure handler for the specific consumer container, you can just implement [KafkaConsumerFailureHandler](src/main/java/com/example/external/consumer/KafkaConsumerFailureHandler.java).
```java
package com.example.person.consumer;

import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import com.example.external.consumer.KafkaConsumerFailureHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomConsumerFailureHandler implements KafkaConsumerFailureHandler {

    @Override
    public RetryTemplate getRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3));

        // create custom RetryTemplate configuration

        return retryTemplate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecoveryCallback<Void> getRecoveryCallback() {
        return context -> {
            // do some business logic.
        };
    }

    @Override
    public String getContainerName() {
        return "containerName"; // the container bean name
    }
}
```