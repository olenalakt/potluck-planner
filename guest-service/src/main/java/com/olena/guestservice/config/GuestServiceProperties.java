package com.olena.guestservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "guest-service.config")
@Data
@NoArgsConstructor
public class GuestServiceProperties {

    private String appVersion;
    private String dbSchemaVersion;

    private String potluckEventProducerTopic;

    private String dishServiceUrl;
    private String drinkServiceUrl;

}
