package com.olena.authserver.config;

import com.olena.authserver.enums.EnvConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
@Slf4j
public class JwtAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private TokenStore tokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Environment environment;

    private Boolean isJwtEnabled() {
        Boolean isJwtEnabled = false;
        String useJwt = EnvConstants.SPRING_SECURITY_OAUTH_JWT_ENABLED.getValue();
        if (useJwt != null && "true".equalsIgnoreCase(useJwt.trim())) {
            isJwtEnabled = true;
        }
        return isJwtEnabled;
    }


    @Bean
    public TokenEnhancer tokenEnhancer() {
        if (isJwtEnabled()) {
            return new CustomJWTEnhancer();
        } else {
            return null;
        }
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        log.info("OL: configure AuthorizationServerEndpointsConfigurer");

        if (isJwtEnabled()) {
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            enhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtConeverter()));
            endpoints.tokenStore(tokenStore()).accessTokenConverter(jwtConeverter()).tokenEnhancer(enhancerChain)
                    .authenticationManager(authenticationManager);

        } else {
            endpoints
                    .authenticationManager(authenticationManager)
                    .approvalStoreDisabled()
                    .tokenStore(tokenStore);
        }
    }

    @Bean
    public TokenStore tokenStore() {
        log.info("OL: tokenStore");

        if (isJwtEnabled()) {
            return new JwtTokenStore(jwtConeverter());
        } else {
            return new InMemoryTokenStore();
        }
    }

    @Bean
    protected JwtAccessTokenConverter jwtConeverter() {
        log.info("OL: jwtConeverter");

        if (isJwtEnabled()) {

            String pwd = environment.getProperty(EnvConstants.SPRING_SECURITY_OAUTH_JWT_KEYSTORE_PASSWORD.getValue());
            String alias = environment.getProperty(EnvConstants.SPRING_SECURITY_OAUTH_JWT_KEYSTORE_ALIAS.getValue());
            String keystore = environment.getProperty(EnvConstants.SPRING_SECURITY_OAUTH_JWT_KEYSTORE_NAME.getValue());
            log.debug("OL: keystore={}, alias={}, pwd={}", keystore, alias, pwd);

            KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keystore),
                    pwd.toCharArray());
            JwtAccessTokenConverter converter = new CustomJWTEncoder(keyStoreKeyFactory.getKeyPair(alias));

            return converter;

        } else {
            return null;
        }

    }

}
