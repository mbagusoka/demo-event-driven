package com.example.external.consumer;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
class KafkaConsumerContainerContextHolder {

    static final String PROPERTIES_PREFIX = "kafka.consumer";

    private List<KafkaConsumerContainerProperty> consumersContainer = new ArrayList<>();

    @Data
    static class KafkaConsumerContainerProperty {

        private String containerName;

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
