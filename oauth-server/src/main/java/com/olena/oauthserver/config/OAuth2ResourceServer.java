package com.olena.oauthserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@Slf4j
public class OAuth2ResourceServer extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        log.debug("OL: OAuth2ResourceServer.configure HttpSecurity");

        http
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/api/v1/**").authenticated()
//                .antMatchers("/events/**").authenticated()
        ;
    }
}
