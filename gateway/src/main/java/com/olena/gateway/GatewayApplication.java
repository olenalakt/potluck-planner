package com.olena.gateway;

import com.olena.gateway.config.GatewayProperties;
import com.olena.gateway.filters.OAuthFilter;
import com.olena.gateway.filters.ThrottlingFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableZuulProxy
@SpringBootApplication
@EnableResourceServer
@Slf4j
public class GatewayApplication {

    @Autowired
    GatewayProperties gatewayProperties;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public OAuthFilter oAuthFilter() {
        return new OAuthFilter();
    }

    @Bean
    public ThrottlingFilter throttlingFilter() {
        return new ThrottlingFilter();
    }

}
