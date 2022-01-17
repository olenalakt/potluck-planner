package com.olena.userservice.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

@Slf4j
public class PotluckPlannerCleanupSerializer implements Serializer<PotluckPlannerCleanup> {

    @Override
    public void configure(Map map, boolean b) {
        // default implementation ignored
    }

    @Override
    public byte[] serialize(String topic, PotluckPlannerCleanup potluckPlannerCleanup) {

        log.debug("serialize, entered: topic=[{}]", topic);

        byte[] potluckPlannerCleanupAsBytes = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            potluckPlannerCleanupAsBytes = objectMapper.writeValueAsString(potluckPlannerCleanup).getBytes();
        } catch (Exception e) {
            log.error("serialize, exception: topic=[{}], [{}]", topic, e.toString());
        }
        return potluckPlannerCleanupAsBytes;
    }

    @Override
    public void close() {
        // default implementation ignored
    }

}
