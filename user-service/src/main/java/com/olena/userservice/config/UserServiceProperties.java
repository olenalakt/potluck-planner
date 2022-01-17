package com.olena.userservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "user-service.config")
@Data
@NoArgsConstructor
public class UserServiceProperties {

    String appVersion;
    String dbSchemaVersion;

}
