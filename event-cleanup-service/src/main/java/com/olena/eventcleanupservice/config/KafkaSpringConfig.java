package com.olena.eventcleanupservice.config;


import com.olena.eventcleanupservice.model.EventCleanupRequestDTO;
import com.olena.eventcleanupservice.service.InitKafkaPropertiesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Properties;


@Configuration
@EnableKafka
@Slf4j
public class KafkaSpringConfig {

    @Autowired
    private KafkaProperties kafkaProperties;
    @Autowired
    private InitKafkaPropertiesService initKafkaPropertiesService;

    @Bean
    public ConsumerFactory<String, EventCleanupRequestDTO> eventCleanupRequestConsumerFactory() {
        log.debug("eventCleanupRequestConsumerFactory entered: kafkaProperties={}", kafkaProperties);
        Properties consumerProperties = initKafkaPropertiesService.getConsumerProperties(kafkaProperties);
        log.debug("eventCleanupRequestConsumerFactory: consumerProperties=[{}]", consumerProperties);

        return new DefaultKafkaConsumerFactory(consumerProperties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventCleanupRequestDTO> eventCleanupRequestKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EventCleanupRequestDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(eventCleanupRequestConsumerFactory());
        return factory;
    }

}
