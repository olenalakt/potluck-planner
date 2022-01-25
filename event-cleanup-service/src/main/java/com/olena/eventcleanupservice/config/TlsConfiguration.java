package com.olena.eventcleanupservice.config;

import com.olena.eventcleanupservice.enums.ConfigConstants;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class TlsConfiguration implements EnvironmentAware {

    // needed for mTLS
    @Override
    public void setEnvironment(final Environment environment) {
        String keystoreLocation = environment.getProperty(ConfigConstants.SERVER_SSL_KEYSTORE.getValue());
        String keystorePassword = environment.getProperty(ConfigConstants.SERVER_SSL_KEYSTOREPASSWORD.getValue());
        String truststoreLocation = keystoreLocation;
        String truststorePassword = keystorePassword;

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
