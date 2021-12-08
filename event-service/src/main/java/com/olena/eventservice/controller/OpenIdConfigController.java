package com.olena.eventservice.controller;

import com.olena.eventservice.config.OidcConfig;
import com.olena.eventservice.model.OidcConfigDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OpenIdConfigController {

    @Autowired
    OidcConfig oidcConfig;

    @GetMapping("/.well-known/openid-configuration")
    // temporary -  until  integrated with gateway
    @CrossOrigin(origins = "http://localhost:4200")
    public OidcConfigDTO getOIDCConfig() {

        log.debug ("OL: getOIDCConfig={}", oidcConfig);

        OidcConfigDTO oidcConfigDTO = new OidcConfigDTO();
        oidcConfigDTO.setToken_endpoint(oidcConfig.getTokenEndpoint());
        oidcConfigDTO.setIssuer(oidcConfig.getTokenIssuer());
        oidcConfigDTO.setAuthorization_endpoint(oidcConfig.getAuthorizationEndpoint());
        oidcConfigDTO.setUserinfo_endpoint(oidcConfig.getUserInfoEndpoint());

        return oidcConfigDTO;
    }

}
