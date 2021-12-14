package com.olena.tokenservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

@SpringBootApplication
//@EnableAuthorizationServer
@Slf4j
public class TokenServiceApplication {

    //TBD - !!! only  for dev env for TLS
    static {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    public static void main(String[] args) {
        SpringApplication.run(TokenServiceApplication.class, args);
    }

}
