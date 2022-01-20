package com.olena.drinkservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "drink-service.config")
@Data
@NoArgsConstructor
public class DrinkServiceProperties {

    private String appVersion;
    private String dbSchemaVersion;

    private String potluckEventProducerTopic;

}
