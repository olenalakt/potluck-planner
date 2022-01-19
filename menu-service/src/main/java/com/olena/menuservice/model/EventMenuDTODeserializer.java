package com.olena.menuservice.model;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class EventMenuDTODeserializer implements Deserializer<EventMenuDTO> {

    @Override
    public void configure(Map<String, ?> arg0, boolean arg1) {
        // default implementation ignored
    }

    @Override
    public EventMenuDTO deserialize(String topic, byte[] payload) {

        log.debug("deserialize, entered: topic=[{}]", topic);

        ObjectMapper mapper = new ObjectMapper();
        EventMenuDTO eventMenuDTO = null;
        try {
            eventMenuDTO = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
                    .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false)
                    .readValue(payload, EventMenuDTO.class);
        } catch (Exception e) {
            log.error("deserialize, exception: topic=[{}], [{}]", topic, e.toString());
        }
        return eventMenuDTO;
    }

    @Override
    public void close() {
        // default implementation ignored
    }

}