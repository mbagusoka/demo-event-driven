package com.example.external.consumer;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
class KafkaConsumerContainerContextHolder {

    static final String PROPERTIES_PREFIX = "kafka.consumer";

    private Map<String, KafkaConsumerContainerProperty> consumersContainer = new HashMap<>();

    @Data
    static class KafkaConsumerContainerProperty {

        private String bootstrapServers;

        private String groupId;

        private int concurrency = 1;

        private String autoOffsetReset = "latest";

        private int maxPollRecords = 500;

        /**
         * In milliseconds.
         */
        private int maxPollInterval = 300000;
    }
}
