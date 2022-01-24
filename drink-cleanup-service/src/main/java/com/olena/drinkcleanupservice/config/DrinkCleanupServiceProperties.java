package com.olena.drinkcleanupservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "drink-cleanup-service.config")
@Data
@NoArgsConstructor
public class DrinkCleanupServiceProperties {

    String appVersion;
    String dbSchemaVersion;

}
