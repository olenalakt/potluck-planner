package com.olena.eventservice.repository.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class PotluckEventSerializer implements Serializer<Event> {

    @Override
    public void configure(Map map, boolean b) {
        // default implementation ignored
    }

    @Override
    public byte[] serialize(String topic, Event potluckEvent) {

        log.debug("serialize, entered: topic=[{}]", topic);

        byte[] messageAsBytes = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            messageAsBytes = objectMapper.writeValueAsString(potluckEvent).getBytes();
        } catch (Exception e) {
            log.error("serialize, exception: topic=[{}], [{}]", topic, e.toString());
        }
        return messageAsBytes;
    }

    @Override
    public void close() {
        // default implementation ignored
    }

}
