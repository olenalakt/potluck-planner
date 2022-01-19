package com.olena.menuservice.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.olena.menuservice.config.KafkaProperties;
import com.olena.menuservice.model.EventDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
@Slf4j
public class EventMenuConsumer {

    @KafkaListener(topics = "${confluent-kafka.config.potluckEventConsumerTopic}", containerFactory = "eventMenuKafkaListenerFactory")
    public void onMessage(ConsumerRecord<String, EventDTO> consumerRecord) throws JsonProcessingException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info("Consume Message : {}", consumerRecord);
        log.info("EventDTO Event is {}", consumerRecord.value());
        EventDTO eventDTO = consumerRecord.value();
        //WebRoomEvent webRoomEvent = (WebRoomEvent) getResponseInstanceFromToAvro(eventDTO, WebRoomEvent.class);
        log.info("eventDTO is {}, Type: {}", eventDTO);
    }

/*    private static Object getResponseInstanceFromToAvro(Object webRoomEventAvro, Class<?> targetClassName) {
        final Schema schema = ReflectData.get().getSchema(targetClassName.getClass());
        Object targetObject = BeanUtils.instantiateClass(targetClassName);
        EventDTO roomEventAvro = (EventDTO) webRoomEventAvro;
        List<String> removedCastErrorProp = Arrays.asList("id", "eventTs");
        roomEventAvro.getSchema().getFields().forEach(d -> {
            if (!removedCastErrorProp.contains(d.name().trim())) {
                PropertyAccessorFactory.forDirectFieldAccess(targetObject).setPropertyValue(d.name(),
                        roomEventAvro.get(d.name()) == null ? roomEventAvro.get(d.name()) :
                                roomEventAvro.get(d.name()).toString());
            } else if ("eventTs".equalsIgnoreCase(d.name().trim())) {
                long eventTsData = new Long(roomEventAvro.get(d.name()).toString());
                Timestamp eventTs = new Timestamp(eventTsData);
                PropertyAccessorFactory.forDirectFieldAccess(targetObject).setPropertyValue(d.name(), eventTs);

            } else if ("id".equalsIgnoreCase(d.name().trim()) && roomEventAvro.get(d.name()) != null) {
                ObjectId objId = new ObjectId(roomEventAvro.get(d.name()).toString());
                PropertyAccessorFactory.forDirectFieldAccess(targetObject).setPropertyValue(d.name(), objId);
            }
        });
        return targetObject;
    }*/


}