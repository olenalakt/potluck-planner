package com.olena.guestservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "guest-service.config")
@Data
@NoArgsConstructor
public class GuestServiceConfig {

    String dbSchemaVersion;

    String dishServiceUrl;
    String drinkServiceUrl;

}
