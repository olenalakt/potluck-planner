package com.olena.menuservice.config;


import com.olena.menuservice.model.EventMenuDTO;
import com.olena.menuservice.service.InitKafkaPropertiesService;
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
    public ConsumerFactory<String, EventMenuDTO> eventMenuConsumerFactory() {
        log.debug("eventMenuConsumerFactory entered: kafkaProperties={}", kafkaProperties);
        Properties consumerProperties = initKafkaPropertiesService.getConsumerProperties(kafkaProperties);
        log.debug("eventMenuConsumerFactory: consumerProperties=[{}]", consumerProperties);

        return new DefaultKafkaConsumerFactory(consumerProperties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EventMenuDTO> eventMenuKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EventMenuDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(eventMenuConsumerFactory());
        return factory;
    }

}
