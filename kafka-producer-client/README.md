# Kafka Producer Client
This library provides mechanism for creating Kafka Producer configurations
automatically for each topic using [RoutingKafkaTemplate](/org/springframework/kafka/core/RoutingKafkaTemplate.java).

## How to Use
* Just add your defined topic producer configuration in application.yml.
```yml
kafka:
  producer:
    topic-producers:
      create-person: # the name of the topic
        bootstrap-servers: localhost:9091,localhost:9093 # comma separated hostname and port of Kafka broker.
        # 0 = Just fire and forget. Producer won't wait for an acknowledgement.
        # 1 = Acknowledgement is sent by the broker when message is successfully written on the leader.
        # all = Acknowledgement is sent by the broker when message is successfully written on all replicas.
        ack: all
      create-person-retry:
        bootstrap-servers: localhost:9092
        ack: all 
```