package com.olena.guestcleanupservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.olena.guestcleanupservice.model.GuestCleanupRequestDTO;
import com.olena.guestcleanupservice.service.GuestCleanupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
@Slf4j
public class GuestCleanupRequestConsumer {

    @Autowired
    GuestCleanupService guestCleanupService;

    @KafkaListener(topics = "${confluent-kafka.config.guestCleanupRequestConsumerTopic}", containerFactory = "guestCleanupRequestKafkaListenerFactory")
    public void onMessage(ConsumerRecord<String, GuestCleanupRequestDTO> consumerRecord) throws JsonProcessingException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.debug("Consume Message : {}", consumerRecord);

        try {
            GuestCleanupRequestDTO guestCleanupRequestDTO = consumerRecord.value();

            guestCleanupService.processCleanupRequest(guestCleanupRequestDTO);

        } catch (Exception e) {
            log.error("KafkaListener.onMessage exception: {}", e.toString());
            throw e;
        }
    }

}