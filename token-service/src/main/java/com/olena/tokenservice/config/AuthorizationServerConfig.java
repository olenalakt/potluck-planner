package com.olena.tokenservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
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
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private TokenStore tokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    TokenServiceProperties tokenServiceProperties;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        log.info("OL: configure AuthorizationServerEndpointsConfigurer, tokenServiceProperties={}", tokenServiceProperties);

        clients.inMemory()
                .withClient(tokenServiceProperties.getAppId())
                .secret("{noop}" + tokenServiceProperties.getAppSecret())
                .scopes("user", "guest")
                .authorizedGrantTypes("client_credentials", "password", "refresh_token")
                .accessTokenValiditySeconds(60000);

        //?                .resourceIds("oauth-server");
        //.and()
        //                .withClient("application2").secret(passwordEncoder.encode("application2secret"))
        //                .authorizedGrantTypes("client_credentials", "password")
        //                .scopes("read")
        //                .accessTokenValiditySeconds(3600)
        //                .resourceIds("sample-oauth")

/*
                        .withClient(environment.getProperty("oath-service.config.appId"))
                .secret("{noop}" + environment.getProperty("oath-service.config.appSecret"))
*/

    }

    // JWT version
   @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomJWTEnhancer();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)  {
		log.info("OL: configure AuthorizationServerEndpointsConfigurer, {}", tokenServiceProperties);

        String isJwtEnabled = tokenServiceProperties.getJwtEnabled();
        log.info("OL: isJwtEnabled={}",isJwtEnabled);
        if (isJwtEnabled != null && "true".equalsIgnoreCase(isJwtEnabled.trim())) {

            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            enhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtConeverter()));
            endpoints.tokenStore(tokenStore()).accessTokenConverter(jwtConeverter()).tokenEnhancer(enhancerChain)
                    .authenticationManager(authenticationManager);
            // endpoints.tokenStore(tokenStore()).tokenEnhancer(jwtConeverter())
            // .authenticationManager(authenticationManager);
        } else {
            endpoints.authenticationManager(authenticationManager);
        }
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		log.info("OL: configure AuthorizationServerSecurityConfigurer");
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public TokenStore tokenStore() {
		log.info("OL: tokenStore");
        String isJwtEnabled = tokenServiceProperties.getJwtEnabled();
        if (isJwtEnabled != null && "true".equalsIgnoreCase(isJwtEnabled.trim())) {
            return new JwtTokenStore(jwtConeverter());
        } else {
            return new InMemoryTokenStore();
        }
    }

    @Bean
    protected JwtAccessTokenConverter jwtConeverter() {
        log.info("OL: jwtConeverter");
        String isJwtEnabled = tokenServiceProperties.getJwtEnabled();
        if (isJwtEnabled != null && "true".equalsIgnoreCase(isJwtEnabled.trim())) {

            String pwd = tokenServiceProperties.getJwtKeystorePassword();
            String alias = tokenServiceProperties.getJwtKeystoreAlias();
            String keystore = tokenServiceProperties.getJwtKeystore();
            log.info("OL: pwd={}", pwd);
            log.info("OL: alias={}", alias);
            log.info("OL: keystore={}", keystore);

            KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keystore),
                    pwd.toCharArray());
            JwtAccessTokenConverter converter = new CustomJWTEncoder(keyStoreKeyFactory.getKeyPair(alias));
            //converter.setKeyPair(keyStoreKeyFactory.getKeyPair(alias));
            return converter;
        } else {
            return null;
        }

    }

}
