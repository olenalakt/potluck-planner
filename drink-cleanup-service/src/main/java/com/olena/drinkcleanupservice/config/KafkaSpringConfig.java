package com.olena.drinkcleanupservice.config;


import com.olena.drinkcleanupservice.model.DrinkCleanupRequestDTO;
import com.olena.drinkcleanupservice.service.InitKafkaPropertiesService;
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
    public ConsumerFactory<String, DrinkCleanupRequestDTO> drinkCleanupRequestConsumerFactory() {
        log.debug("drinkCleanupRequestConsumerFactory entered: kafkaProperties={}", kafkaProperties);
        Properties consumerProperties = initKafkaPropertiesService.getConsumerProperties(kafkaProperties);
        log.debug("drinkCleanupRequestConsumerFactory: consumerProperties=[{}]", consumerProperties);

        return new DefaultKafkaConsumerFactory(consumerProperties);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DrinkCleanupRequestDTO> drinkCleanupRequestKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, DrinkCleanupRequestDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(drinkCleanupRequestConsumerFactory());
        return factory;
    }

}
