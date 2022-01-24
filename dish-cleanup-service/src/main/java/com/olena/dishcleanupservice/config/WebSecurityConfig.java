package com.olena.dishcleanupservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@Slf4j
public class WebSecurityConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    CorsAndSecurityProperties corsAndSecurityProperties;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.anonymous().
                and().authorizeRequests().antMatchers("/actuator/**").permitAll().
                and().authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/v1/dishcleanup/**").permitAll().
                anyRequest().authenticated();

        if (corsAndSecurityProperties.getContentSecurityPolicy() != null) {
            httpSecurity.headers().contentSecurityPolicy(corsAndSecurityProperties.getContentSecurityPolicy());
        }
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        log.debug("corsConfigurationSource, entered: accessControlAllowCredentials=[{}], accessControlAllowOriginList=[{}]",
                corsAndSecurityProperties.getAccessControlAllowCredentials(),
                corsAndSecurityProperties.getAccessControlAllowOriginList());

        RegexCorsConfiguration configuration = new RegexCorsConfiguration();
        configuration.applyPermitDefaultValues();
        List<String> accessControlAllowOrigin = corsAndSecurityProperties.getAccessControlAllowOriginList() == null ? Arrays.asList("*") : corsAndSecurityProperties.getAccessControlAllowOriginList();
        Boolean accessControlAllowCredentials = corsAndSecurityProperties.getAccessControlAllowCredentials();

        log.debug("corsConfigurationSource, set: accessControlAllowCredentials=[{}], accessControlAllowOriginList=[{}]",
                accessControlAllowCredentials,
                accessControlAllowOrigin);

        configuration.setAllowedOriginRegex(accessControlAllowOrigin);
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowCredentials(accessControlAllowCredentials);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

    }
}
