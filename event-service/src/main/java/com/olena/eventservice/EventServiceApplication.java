package com.olena.eventservice;

import com.olena.eventservice.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

@SpringBootApplication
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableResourceServer
public class EventServiceApplication {


    //TODO - !!! only  for dev env
    static {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    @Autowired
    EventRepository eventRepository;

    public static void main(String[] args) {
        SpringApplication.run(EventServiceApplication.class, args);
    }

}
