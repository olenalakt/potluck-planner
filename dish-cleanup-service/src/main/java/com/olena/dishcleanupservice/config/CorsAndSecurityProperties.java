package com.olena.dishcleanupservice.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "dish-service.cors-config")
@Data
@NoArgsConstructor
public class CorsAndSecurityProperties {

    private List<String> accessControlAllowOriginList;
    private Boolean accessControlAllowCredentials;
    private String contentSecurityPolicy;

}
