package com.olena.gateway.model;

import lombok.Data;

@Data
public class OidcConfigDTO {

    private String token_endpoint;

    private String issuer;

    private String authorization_endpoint;

    private String userinfo_endpoint;

}
