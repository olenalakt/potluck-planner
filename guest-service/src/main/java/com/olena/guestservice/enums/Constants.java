package com.olena.guestservice.enums;

public enum Constants {

    // part of path to  dishes service
    GUEST("guest"),
    ;

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}