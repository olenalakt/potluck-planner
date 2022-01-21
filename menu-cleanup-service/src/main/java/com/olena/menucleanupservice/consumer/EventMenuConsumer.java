package com.olena.menucleanupservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.olena.menucleanupservice.model.MenuCleanupRequestDTO;
import com.olena.menucleanupservice.service.MenuCleanupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
@Slf4j
public class EventMenuConsumer {

    @Autowired
    MenuCleanupService menuCleanupService;

    @KafkaListener(topics = "${confluent-kafka.config.menuCleanupRequestConsumerTopic}", containerFactory = "menuCleanupRequestKafkaListenerFactory")
    public void onMessage(ConsumerRecord<String, MenuCleanupRequestDTO> consumerRecord) throws JsonProcessingException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.debug("Consume Message : {}", consumerRecord);

        try {
            MenuCleanupRequestDTO menuCleanupRequestDTO = consumerRecord.value();

            menuCleanupService.processMenuCleanupRequest(menuCleanupRequestDTO);

        } catch (Exception e) {
            log.error("KafkaListener.onMessage exception: {}", e.toString());
            throw e;
        }
    }

}