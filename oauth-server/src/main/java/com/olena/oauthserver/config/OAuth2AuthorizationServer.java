package com.olena.oauthserver.config;

import com.olena.oauthserver.enums.EnvConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    // JWT related
    private TokenStore tokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;
    // end JWT related

    @Autowired
    private OauthServerProperties oauthServerProperties;

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
        log.debug("OL: configure ClientDetailsServiceConfigurer, oauthServerProperties={}",
                oauthServerProperties
        );

        clients
                .inMemory()
                // original
                .withClient(oauthServerProperties.getClientAppId())
                .secret(passwordEncoder.encode(oauthServerProperties.getClientAppSecret()))
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .authorities("READ_ONLY_CLIENT")
                .scopes("openid", "read_profile_info")
                .resourceIds("oauth2-resource")
                .redirectUris(oauthServerProperties.getClientRedirectUri())
                .accessTokenValiditySeconds(5000)
                .refreshTokenValiditySeconds(50000)
                .and()
                .withClient(oauthServerProperties.getResourceServerId())
                .secret(passwordEncoder.encode(oauthServerProperties.getResourceServerSecret()))
                .authorizedGrantTypes("password",  "refresh_token")
                .redirectUris(oauthServerProperties.getResourceGuestRedirectUri())
                .accessTokenValiditySeconds(5000)
                .refreshTokenValiditySeconds(50000)
                .scopes("openid", "read_profile_info", "user", "guest")
                // TODO remove -  temporary, looks like caching issue
                .and()
                .withClient("application1")
                .secret(passwordEncoder.encode("application1secret"))
        ;

    }

    // JWT version
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomJWTEnhancer();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)  {
        log.info("OL: configure AuthorizationServerEndpointsConfigurer, {}", oauthServerProperties);

        String isJwtEnabled = oauthServerProperties.getJwtEnabled();
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



    @Bean
    public TokenStore tokenStore() {
        log.info("OL: tokenStore");
        String isJwtEnabled = oauthServerProperties.getJwtEnabled();
        if (isJwtEnabled != null && "true".equalsIgnoreCase(isJwtEnabled.trim())) {
            return new JwtTokenStore(jwtConeverter());
        } else {
            return new InMemoryTokenStore();
        }
    }

    @Bean
    protected JwtAccessTokenConverter jwtConeverter() {
        log.info("OL: jwtConeverter");
        String isJwtEnabled = oauthServerProperties.getJwtEnabled();
        if (isJwtEnabled != null && "true".equalsIgnoreCase(isJwtEnabled.trim())) {

            String pwd = oauthServerProperties.getJwtKeystorePassword();
            String alias = oauthServerProperties.getJwtKeystoreAlias();
            String keystore = oauthServerProperties.getJwtKeystore();
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