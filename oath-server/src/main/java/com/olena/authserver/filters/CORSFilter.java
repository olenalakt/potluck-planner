package com.olena.authserver.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
//    TODO  -  remove if integrated with gateway
public class CORSFilter extends CorsFilter {

    public CORSFilter() {
        super(configurationSource());
    }

    private static UrlBasedCorsConfigurationSource configurationSource() {

        log.debug("OL: CORS configurationSource");

        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://localhost:4200");
        config.setAllowCredentials(true);
        config.addAllowedHeader("Authentication");
        config.addAllowedHeader("Content-Type");
        config.addAllowedMethod(HttpMethod.OPTIONS);
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/oauth/token", config);
        return source;
    }

}
