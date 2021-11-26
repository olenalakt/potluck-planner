package com.olena.eventservice;

import com.olena.eventservice.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.File;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableResourceServer
public class EventServiceApplication {

    @Autowired
    EventRepository eventRepository;

        //TBD -  make configuration external to  app
        /*
        String path = System.getProperty("user.dir");
        System.setProperty("javax.net.ssl.trustStore", path + File.separator + "trust-store.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "springboot");
        System.setProperty("javax.net.ssl.keyStore",  path + File.separator + "eventservice.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "olena123");
*/

    //TBD - !!! only  for dev env
    static {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }


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
