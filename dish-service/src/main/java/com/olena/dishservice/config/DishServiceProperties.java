package com.olena.dishservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dish-service.config")
@Data
@NoArgsConstructor
public class DishServiceProperties {

    private String dbSchemaVersion;
    private String appVersion;

    private String potluckEventProducerTopic;

}
