package com.olena.menuservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.olena.menuservice.model.EventMenuDTO;
import com.olena.menuservice.service.MenuService;
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
    MenuService menuService;

    @KafkaListener(topics = "${confluent-kafka.config.potluckEventConsumerTopic}", containerFactory = "eventMenuKafkaListenerFactory")
    public void onMessage(ConsumerRecord<String, EventMenuDTO> consumerRecord) throws JsonProcessingException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.debug("Consume Message : {}", consumerRecord);

        try {
            EventMenuDTO eventMenuDTO = consumerRecord.value();

            menuService.processEventMenuDTO(eventMenuDTO);

        } catch (Exception e) {
            log.error("KafkaListener.onMessage exception: {}", e.toString());
            throw e;
        }
    }

}