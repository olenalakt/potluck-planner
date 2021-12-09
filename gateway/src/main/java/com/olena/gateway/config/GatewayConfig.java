package com.olena.gateway.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "gateway.config")
@Data
@NoArgsConstructor
public class GatewayConfig {

    String appVersion;

}
