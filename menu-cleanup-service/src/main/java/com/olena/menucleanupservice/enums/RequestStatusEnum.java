package com.olena.menucleanupservice.enums;

public enum RequestStatusEnum {

    PENDING("PENDING"),
    INVALID("INVALID"),
    FAILED("FAILED"),
    PROCESSED("PROCESSED");

    private final String value;

    RequestStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
