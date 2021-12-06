package com.olena.gateway;

import com.olena.gateway.config.CorsAndSecurityProperties;
import com.olena.gateway.config.RegexCorsConfiguration;
import com.olena.gateway.filters.OAuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@EnableZuulProxy
@SpringBootApplication
//@EnableConfigurationProperties(CorsAndSecurityProperties.class)
@Slf4j
public class GatewayApplication {

    @Autowired
    CorsAndSecurityProperties corsAndSecurityProperties;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public OAuthFilter oAuthFilter() {
        return new OAuthFilter();
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

/*    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/books/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST");
            }
        };
    }*/
}
