package com.olena.oauthserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.util.Collections;
import java.util.Map;

@Slf4j
public class CustomJWTEncoder extends JwtAccessTokenConverter {

    final RsaSigner signer;
    private final JsonParser objectMapper = JsonParserFactory.create();

    public CustomJWTEncoder(KeyPair keyPair) {

        super();
        super.setKeyPair(keyPair);
        this.signer = new RsaSigner((RSAPrivateKey) keyPair.getPrivate());
        log.info("OL: CustomJWTEncoder constructed");
    }

    @Override
    protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        log.info("OL: OAuth2AccessToken encode");

        String content;
        try {
            content = this.objectMapper
                    .formatMap(getAccessTokenConverter().convertAccessToken(accessToken, authentication));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot convert access token to JSON", ex);
        }
        Map<String, String> customHeaders = Collections.singletonMap("kid", "d7d87567-1840-4f45-9614-49071fca4d21");
        String token = JwtHelper.encode(content, this.signer, customHeaders).getEncoded();
        return token;
    }

}