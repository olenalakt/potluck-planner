package com.olena.eventservice.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Slf4j
@Service
public class PotluckEventPublisher {

    public <T extends Object> void publish(Producer<String, T> producer, String topic, T message) {
        //source.output().send(MessageBuilder.withPayload(event).build());

        log.debug("publish, entered: topic=[{}], message=[{}]", topic, message);

        Future<RecordMetadata> record = send(producer, topic, message);
        try {

            RecordMetadata metadata = record.get();
            log.debug("publish, response: topic=[{}], message=[{}], metadata=[{}]", topic, message, metadata);

        } catch (Exception e) {

            // monitor those in NewRelic -  improve  handleException later
            log.error("publish, exception: [{}]", e.toString());
        }

    }

    private <T extends Object> Future<RecordMetadata> send(Producer<String, T> producer, String topic, T message) {
        log.debug("send, entered: topic=[{}], message=[{}]", topic, message);
        ProducerRecord<String, T> record = new ProducerRecord<>(topic, message);
        return producer.send(record);
    }
}
