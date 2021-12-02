package com.olena.eventservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class TlsConfiguration implements EnvironmentAware {

    @Override
    public void setEnvironment(final Environment environment) {
        log.debug("OL: setEnvironment: {}", environment);

        String keystoreLocation = environment.getProperty("server.ssl.key-store");
        String keystorePassword = environment.getProperty("server.ssl.key-store-password");
        String truststoreLocation = environment.getProperty("server.ssl.key-store");
        String truststorePassword = environment.getProperty("server.ssl.key-store-password");

        if (truststoreLocation != null && truststorePassword != null) {
            System.setProperty("javax.net.ssl.trustStore", truststoreLocation);
            System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);
        }

        if (keystoreLocation != null && keystorePassword != null) {
            System.setProperty("javax.net.ssl.keyStore", keystoreLocation);
            System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);
        }

    }
}
