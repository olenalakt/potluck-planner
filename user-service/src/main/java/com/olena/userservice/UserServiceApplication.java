package com.olena.userservice;

import com.olena.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableResourceServer
public class UserServiceApplication {

    @Autowired
    UserRepository userRepository;

    /*
    static {
        String path = System.getProperty("user.dir");
        System.setProperty("javax.net.ssl.trustStore", path + File.separator + "trust-store.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "springboot");

        System.setProperty("javax.net.ssl.keyStore",  path + File.separator + "keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "springboot");

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }




    @Bean
    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
                                                 OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }
*/

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
