package com.olena.dishservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "event-service.config")
@Data
@NoArgsConstructor
public class DishServiceConfig {

    String dbSchemaVersion;

}
