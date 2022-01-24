package com.olena.dishcleanupservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "dish-cleanup-service.config")
@Data
@NoArgsConstructor
public class DishCleanupServiceProperties {

    String appVersion;
    String dbSchemaVersion;

}
