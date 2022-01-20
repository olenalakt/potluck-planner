package com.olena.dishservice.config;

import com.olena.dishservice.model.DishMessage;
import com.olena.dishservice.service.InitKafkaPropertiesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@Slf4j
public class KafkaSpringConfig {

    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired
    private InitKafkaPropertiesService initKafkaPropertiesService;

    @Bean
    public Properties producerProperties() {
        return initKafkaPropertiesService.getProducerProperties(kafkaProperties);
    }

    @Bean(name = "potluckEventProducer")
    public Producer<String, DishMessage> startPotluckEventProducer(Properties producerProperties) {
//        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getValueSerializerCLass());
        log.debug("startPotluckEventProducer: Properties=[{}]", producerProperties);
        return new KafkaProducer<String, DishMessage>(producerProperties);
    }

}
