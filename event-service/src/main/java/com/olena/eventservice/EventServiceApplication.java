package com.olena.eventservice;

import com.olena.eventservice.repository.EventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.File;

@EnableResourceServer
//@EnableOAuth2Client
@SpringBootApplication
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class EventServiceApplication {

    //TODO - !!! only  for dev env

    static {

        String path = System.getProperty("user.dir");
        log.debug("OL: path: {}", path);

        System.setProperty("javax.net.ssl.trustStore", path + File.separator + "trust-store.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "springboot");
        log.debug("OL: trustStore: {}", System.getProperty("javax.net.ssl.trustStore"));

        System.setProperty("javax.net.ssl.keyStore", path + File.separator + "keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "springboot");
        log.debug("OL: trustStore: {}", System.getProperty("javax.net.ssl.keyStore"));

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    @Autowired
    EventRepository eventRepository;

/*
    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
                                                 OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }
*/

    public static void main(String[] args) {
        SpringApplication.run(EventServiceApplication.class, args);
    }

}
