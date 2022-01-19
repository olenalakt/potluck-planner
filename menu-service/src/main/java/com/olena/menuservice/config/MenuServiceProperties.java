package com.olena.menuservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "event-service.config")
@Data
@NoArgsConstructor
public class MenuServiceProperties {

    String appVersion;
    String dbSchemaVersion;

}
