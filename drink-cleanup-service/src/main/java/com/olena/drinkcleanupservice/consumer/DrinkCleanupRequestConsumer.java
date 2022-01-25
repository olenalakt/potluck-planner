package com.olena.drinkcleanupservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.olena.drinkcleanupservice.model.DrinkCleanupRequestDTO;
import com.olena.drinkcleanupservice.service.DrinkCleanupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
@Slf4j
public class DrinkCleanupRequestConsumer {

    @Autowired
    DrinkCleanupService drinkCleanupService;

    @KafkaListener(topics = "${confluent-kafka.config.drinkCleanupRequestConsumerTopic}", containerFactory = "drinkCleanupRequestKafkaListenerFactory")
    public void onMessage(ConsumerRecord<String, DrinkCleanupRequestDTO> consumerRecord) throws JsonProcessingException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.debug("Consume Message : {}", consumerRecord);

        try {
            DrinkCleanupRequestDTO drinkCleanupRequestDTO = consumerRecord.value();

            drinkCleanupService.processCleanupRequest(drinkCleanupRequestDTO);

        } catch (Exception e) {
            log.error("KafkaListener.onMessage exception: {}", e.toString());
            throw e;
        }
    }

}