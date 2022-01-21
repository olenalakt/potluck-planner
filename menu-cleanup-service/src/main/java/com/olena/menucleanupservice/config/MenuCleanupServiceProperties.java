package com.olena.menucleanupservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "menu-cleanup-service.config")
@Data
@NoArgsConstructor
public class MenuCleanupServiceProperties {

    String appVersion;
    String dbSchemaVersion;

}
