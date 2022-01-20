package com.olena.dishservice.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class DishMessageSerializer implements Serializer<DishMessage> {

    @Override
    public void configure(Map map, boolean b) {
        // default implementation ignored
    }

    @Override
    public byte[] serialize(String topic, DishMessage dishMessage) {

        log.debug("serialize, entered: topic=[{}]", topic);

        byte[] messageAsBytes = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            messageAsBytes = objectMapper.writeValueAsString(dishMessage).getBytes();
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
