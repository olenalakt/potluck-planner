package com.olena.eventcleanupservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "event-cleanup-service.config")
@Data
@NoArgsConstructor
public class EventCleanupServiceProperties {

    String appVersion;
    String dbSchemaVersion;

}
