package com.olena.eventservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

import static com.olena.eventservice.enums.Constants.GUEST_SERVICE;

@Configuration
@ConfigurationProperties(prefix = "event-service.config")
@Data
@NoArgsConstructor
public class EventServiceConfig {

    String dbSchemaVersion;

    String guestServiceUrl;

}
