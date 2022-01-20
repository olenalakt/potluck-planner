package com.olena.guestservice.config;


import com.olena.guestservice.model.GuestMessage;
import com.olena.guestservice.service.InitKafkaPropertiesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.modelmapper.ModelMapper;
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
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Properties producerProperties() {
        return initKafkaPropertiesService.getProducerProperties(kafkaProperties);
    }

    @Bean(name = "potluckEventProducer")
    public Producer<String, GuestMessage> startPotluckEventProducer(Properties producerProperties) {
//        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getValueSerializerCLass());
        log.debug("startPotluckEventProducer: Properties=[{}]", producerProperties);
        return new KafkaProducer<String, GuestMessage>(producerProperties);
    }

}
