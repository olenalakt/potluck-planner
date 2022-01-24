package com.olena.dishcleanupservice.model;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class DishCleanupRequestDTODeserializer implements Deserializer<DishCleanupRequestDTO> {

    @Override
    public void configure(Map<String, ?> arg0, boolean arg1) {
        // default implementation ignored
    }

    @Override
    public DishCleanupRequestDTO deserialize(String topic, byte[] payload) {

        log.debug("deserialize, entered: topic=[{}]", topic);

        ObjectMapper mapper = new ObjectMapper();
        DishCleanupRequestDTO dishCleanupRequestDTO = null;
        try {
            dishCleanupRequestDTO = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
                    .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false)
                    .readValue(payload, DishCleanupRequestDTO.class);
        } catch (Exception e) {
            log.error("deserialize, exception: topic=[{}], [{}]", topic, e.toString());
        }
        return dishCleanupRequestDTO;
    }

    @Override
    public void close() {
        // default implementation ignored
    }

}