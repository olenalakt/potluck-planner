package com.olena.gateway.controller;

import com.olena.gateway.config.OidcProperties;
import com.olena.gateway.model.OidcConfigDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OpenIdConfigController {

    @Autowired
    OidcProperties oidcProperties;

    @GetMapping("/.well-known/openid-configuration")
// TODO -  get rid of @CrossOrigin -  should use CORS regex external config instead
    @CrossOrigin(origins = "http://localhost:4200")
    public OidcConfigDTO getOIDCConfig() {

        log.debug("OL: oidcProperties={}", oidcProperties);

        OidcConfigDTO oidcConfigDTO = new OidcConfigDTO();
        oidcConfigDTO.setToken_endpoint(oidcProperties.getTokenEndpoint());
        oidcConfigDTO.setIssuer(oidcProperties.getTokenIssuer());
        oidcConfigDTO.setAuthorization_endpoint(oidcProperties.getAuthorizationEndpoint());
        oidcConfigDTO.setUserinfo_endpoint(oidcProperties.getUserInfoEndpoint());

        return oidcConfigDTO;
    }

}
