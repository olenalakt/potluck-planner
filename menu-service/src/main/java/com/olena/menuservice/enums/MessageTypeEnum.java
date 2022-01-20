package com.olena.menuservice.enums;

public enum MessageTypeEnum {

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
