package com.olena.guestservice;

import com.olena.guestservice.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

@SpringBootApplication
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableResourceServer
public class GuestServiceApplication {

    //TBD - !!! only  for dev env
    static {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    @Autowired
    GuestRepository guestRepository;

    public static void main(String[] args) {
        SpringApplication.run(GuestServiceApplication.class, args);
    }

}
