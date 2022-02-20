package com.example.person.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "person")
public class PersonTopicProperties implements PersonTopicContextHolder {

    String createTopic;
}
