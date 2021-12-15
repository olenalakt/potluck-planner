package com.olena.oauthserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomJWTEnhancer extends JwtAccessTokenConverter {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        log.info("OL: OAuth2AccessToken enhance");

        Map info = new HashMap();
        info.put("ts", Instant.now().getEpochSecond());
        info.put("userName", authentication.getUserAuthentication().getName());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }

}