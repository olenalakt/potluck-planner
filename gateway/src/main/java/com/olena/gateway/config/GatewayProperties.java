package com.olena.gateway.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "gateway.config")
@Data
@NoArgsConstructor
public class GatewayProperties {

    String appVersion;

    String oathIntrospectionEndpoint;

    // throttling props
    // max requests
    Integer maxUserRequestsPerSec;
    // TODO  -  not implemented per service
    Integer maxEventRequestsPerSec;
    Integer maxGuestRequestsPerSec;
    Integer maxMenuRequestsPerSec;

}
