package com.olena.menuservice;

import com.olena.menuservice.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

@SpringBootApplication
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableResourceServer
//@EnableBinding(Source.class)
public class MenuServiceApplication {


    //TODO - !!! only  for dev env
    static {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    @Autowired
    MenuRepository menuRepository;

    public static void main(String[] args) {
        SpringApplication.run(MenuServiceApplication.class, args);
    }

}
