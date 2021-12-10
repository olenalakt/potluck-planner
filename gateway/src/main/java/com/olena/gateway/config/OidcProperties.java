package com.olena.gateway.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oidc")
@Data
@NoArgsConstructor
public class OidcProperties {

    String tokenEndpoint;
    String tokenIssuer;
    String authorizationEndpoint;
    String userInfoEndpoint;

}
