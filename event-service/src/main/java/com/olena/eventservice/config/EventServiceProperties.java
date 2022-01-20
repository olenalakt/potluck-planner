package com.olena.eventservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "event-service.config")
@Data
@NoArgsConstructor
public class EventServiceProperties {

    private String appVersion;
    private String dbSchemaVersion;

    private String potluckEventProducerTopic;

    private String guestServiceUrl;

}
