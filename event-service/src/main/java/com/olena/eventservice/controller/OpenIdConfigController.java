package com.olena.eventservice.controller;

import com.olena.eventservice.model.OIDCConfig;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenIdConfigController {

    @GetMapping("/.well-known/openid-configuration")
    @CrossOrigin(origins = "http://localhost:4200")
    public OIDCConfig getOIDCConfig() {

        OIDCConfig oidcConfig = new OIDCConfig();
        oidcConfig.setToken_endpoint("http://localhost:8080/oauth/token");
        oidcConfig.setIssuer("http://localhost:8080/");
        oidcConfig.setAuthorization_endpoint("http://localhost:8080/oauth/authorize");
        oidcConfig.setUserinfo_endpoint("http://localhost:8080/api/users/me");

        return oidcConfig;
    }

}
