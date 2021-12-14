package com.olena.tokenservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "token-service.config")
@Data
@NoArgsConstructor
public class TokenServiceProperties {

    String appVersion;

    String oathIntrospectionEndpoint;

}
