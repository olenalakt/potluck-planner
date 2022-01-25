package com.olena.eventcleanupservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.olena.eventcleanupservice.model.EventCleanupRequestDTO;
import com.olena.eventcleanupservice.service.EventCleanupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
@Slf4j
public class EventCleanupRequestConsumer {

    @Autowired
    EventCleanupService eventCleanupService;

    @KafkaListener(topics = "${confluent-kafka.config.eventCleanupRequestConsumerTopic}", containerFactory = "eventCleanupRequestKafkaListenerFactory")
    public void onMessage(ConsumerRecord<String, EventCleanupRequestDTO> consumerRecord) throws JsonProcessingException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.debug("Consume Message : {}", consumerRecord);

        try {
            EventCleanupRequestDTO eventCleanupRequestDTO = consumerRecord.value();

            eventCleanupService.processCleanupRequest(eventCleanupRequestDTO);

        } catch (Exception e) {
            log.error("KafkaListener.onMessage exception: {}", e.toString());
            throw e;
        }
    }

}