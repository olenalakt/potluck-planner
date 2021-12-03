package com.olena.authserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
@Slf4j
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private TokenStore tokenStore;
//    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

/*
    public AuthorizationServerConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
*/

    @Autowired
    private Environment environment;


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .approvalStoreDisabled()
                .tokenStore(tokenStore);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        log.info("OL: configure AuthorizationServerEndpointsConfigurer, {}", environment.getProperty("spring.security.oauth.jwt.keystore.alias"));

        clients.inMemory()
                .withClient("application1").secret("{noop}" +"application1secret")
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

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        log.info("OL: configure AuthorizationServerSecurityConfigurer");
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }
/*
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        log.info("OL: configure AuthorizationServerSecurityConfigurer");
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }
  */
    // JWT version
/*
   @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomJWTEnhancer();
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		log.info("OL: configure AuthorizationServerEndpointsConfigurer, {}", environment.getProperty("spring.security.oauth.jwt.keystore.alias"));

        String useJwt = environment.getProperty("spring.security.oauth.jwt");
        log.info("OL: useJwt={}",useJwt);
        if (useJwt != null && "true".equalsIgnoreCase(useJwt.trim())) {

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

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		log.info("OL: configure ClientDetailsServiceConfigurer");
        //TBD -  move to  external config
        clients.inMemory()
                .withClient(environment.getProperty("oath-service.config.appId"))
                .secret("{noop}" + environment.getProperty("oath-service.config.appSecret"))
                .scopes("user", "guest")
                .authorizedGrantTypes("client_credentials", "password", "refresh_token")
                .accessTokenValiditySeconds(6000);
    }

    @Bean
    public TokenStore tokenStore() {
		log.info("OL: tokenStore");
        String useJwt = environment.getProperty("spring.security.oauth.jwt");
        if (useJwt != null && "true".equalsIgnoreCase(useJwt.trim())) {
            return new JwtTokenStore(jwtConeverter());
        } else {
            return new InMemoryTokenStore();
        }
    }

    @Bean
    protected JwtAccessTokenConverter jwtConeverter() {
		log.info("OL: jwtConeverter");
        String pwd = environment.getProperty("spring.security.oauth.jwt.keystore.password");
        String alias = environment.getProperty("spring.security.oauth.jwt.keystore.alias");
        String keystore = environment.getProperty("spring.security.oauth.jwt.keystore.name");
        log.info("OL: pwd={}", pwd);
        log.info("OL: alias={}", alias);
        log.info("OL: keystore={}", keystore);

        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keystore),
                pwd.toCharArray());
        JwtAccessTokenConverter converter = new CustomJWTEncoder(keyStoreKeyFactory.getKeyPair(alias));
        //converter.setKeyPair(keyStoreKeyFactory.getKeyPair(alias));
        return converter;
    }
    */
}
