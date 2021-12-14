package com.olena.oauthserver.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oauth-server.config")
@Data
@NoArgsConstructor
public class OauthServerProperties {

    String appVersion;
    String appDbSchemaVersion;

/*

    String jwtEnabled;
    String jwtKeystore;
    String jwtKeystorePassword;
    String jwtKeystoreAlias;
*/

    String clientAppId;
    String clientAppSecret;
    String clientRedirectUri;

    String resourceServerId;
    String resourceServerSecret;
}
