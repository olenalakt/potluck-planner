package com.olena.eventcleanupservice.enums;

public enum ConfigConstants {

    SERVER_SSL_KEYSTORE("server.ssl.key-store"),
    SERVER_SSL_KEYSTOREPASSWORD("server.ssl.key-store-password");

    private final String value;

    ConfigConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
