package com.olena.dishcleanupservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.olena.dishcleanupservice.model.DishCleanupRequestDTO;
import com.olena.dishcleanupservice.service.DishCleanupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
@Slf4j
public class DishCleanupRequestConsumer {

    @Autowired
    DishCleanupService dishCleanupService;

    @KafkaListener(topics = "${confluent-kafka.config.dishCleanupRequestConsumerTopic}", containerFactory = "dishCleanupRequestKafkaListenerFactory")
    public void onMessage(ConsumerRecord<String, DishCleanupRequestDTO> consumerRecord) throws JsonProcessingException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.debug("Consume Message : {}", consumerRecord);

        try {
            DishCleanupRequestDTO dishCleanupRequestDTO = consumerRecord.value();

            dishCleanupService.processCleanupRequest(dishCleanupRequestDTO);

        } catch (Exception e) {
            log.error("KafkaListener.onMessage exception: {}", e.toString());
            throw e;
        }
    }

}