package com.olena.oathserver.config;

import com.olena.oathserver.enums.EnvConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
@Slf4j
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private Environment environment;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        log.debug("OL: OAuth2AuthorizationServer.configure AuthorizationServerSecurityConfigurer");

        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        log.debug("OL: configure ClientDetailsServiceConfigurer, clientAppId={}/{}, urlRedirect={}",
                environment.getProperty(EnvConstants.OATHSERVICE_CONFIG_CLIENTAPPID.getValue()),
                environment.getProperty(EnvConstants.OATHSERVICE_CONFIG_CLIENTAPPSECRET.getValue()),
                environment.getProperty(EnvConstants.OATHSERVICE_CONFIG_CLIENTREDIRECTURI.getValue())
        );

        clients
                .inMemory()
                // original
                .withClient(environment.getProperty(EnvConstants.OATHSERVICE_CONFIG_CLIENTAPPID.getValue()))
                .secret(passwordEncoder.encode(environment.getProperty(EnvConstants.OATHSERVICE_CONFIG_CLIENTAPPSECRET.getValue())))
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .authorities("READ_ONLY_CLIENT")
                .scopes("openid", "read_profile_info")
                .resourceIds("oauth2-resource")
                .redirectUris(environment.getProperty(EnvConstants.OATHSERVICE_CONFIG_CLIENTREDIRECTURI.getValue()))
                .accessTokenValiditySeconds(5000)
                .refreshTokenValiditySeconds(50000)
                .and()
                .withClient(environment.getProperty(EnvConstants.OATHSERVICE_CONFIG_RESOURCESERVERID.getValue()))
                .secret(passwordEncoder.encode(environment.getProperty(EnvConstants.OATHSERVICE_CONFIG_RESOURCESERVERSECRET.getValue())));

/*
        clients.inMemory()
                .withClient(environment.getProperty("oath-service.config.clientAppId"))
 //               .secret("{noop}" + environment.getProperty("oath-service.config.appSecret"))
                .secret(passwordEncoder.encode(environment.getProperty("oath-service.config.clientAppSecret")))
// !!! - TODO -  figure out  how to  allow update data
                .authorizedGrantTypes("authorization_code", "password", "refresh_token")
                .authorities("READ_ONLY_CLIENT")
                .scopes( "openid", "read_profile_info")
                .resourceIds("oauth2-resource")
                .redirectUris(environment.getProperty("oath-service.config.clientRedirectUri"))
                .accessTokenValiditySeconds(5000)
                .refreshTokenValiditySeconds(50000)
                .and()
                .withClient(environment.getProperty("oath-service.config.serviceAppId"))
//                .secret("{noop}" + environment.getProperty("oath-service.config.serviceAppSecret"))
                .secret(passwordEncoder.encode(environment.getProperty("oath-service.config.serviceAppSecret")))
//                .authorizedGrantTypes("client_credentials", "password", "refresh_token")
//                .scopes("user", "guest", "openid", "read_profile_info")
                .accessTokenValiditySeconds(60000)
//                .resourceIds("oauth-server")
        ;

*/


    }

}