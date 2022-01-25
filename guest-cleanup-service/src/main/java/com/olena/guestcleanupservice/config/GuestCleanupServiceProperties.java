package com.olena.guestcleanupservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "guest-cleanup-service.config")
@Data
@NoArgsConstructor
public class GuestCleanupServiceProperties {

    String appVersion;
    String dbSchemaVersion;

}
