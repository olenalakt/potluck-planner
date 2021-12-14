package com.olena.oauthserver.enums;

public enum EnvConstants {

    OATHSERVICE_CONFIG_CLIENTAPPID("oath-service.config.clientAppId"),
    OATHSERVICE_CONFIG_CLIENTAPPSECRET("oath-service.config.clientAppSecret"),
    OATHSERVICE_CONFIG_CLIENTREDIRECTURI("oath-service.config.clientRedirectUri"),
    OATHSERVICE_CONFIG_RESOURCESERVERID("oath-service.config.resourceServerId"),
    OATHSERVICE_CONFIG_RESOURCESERVERSECRET("oath-service.config.resourceServerSecret"),

    // JWT  config
    SPRING_SECURITY_OAUTH_JWT_ENABLED("spring.security.oauth.jwt.enabled"),
    SPRING_SECURITY_OAUTH_JWT_KEYSTORE_PASSWORD("spring.security.oauth.jwt.keystore.password"),
    SPRING_SECURITY_OAUTH_JWT_KEYSTORE_ALIAS("spring.security.oauth.jwt.keystore.alias"),
    SPRING_SECURITY_OAUTH_JWT_KEYSTORE_NAME("spring.security.oauth.jwt.keystore.name"),
    ;

    private final String value;

    EnvConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
