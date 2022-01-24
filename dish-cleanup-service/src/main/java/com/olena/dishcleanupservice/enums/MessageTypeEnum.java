package com.olena.dishcleanupservice.enums;

public enum MessageTypeEnum {

    USER("user"),
    EVENT("event"),
    GUEST("guest"),
    DISH("dish"),
    DRINK("drink");

    private final String value;

    MessageTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
