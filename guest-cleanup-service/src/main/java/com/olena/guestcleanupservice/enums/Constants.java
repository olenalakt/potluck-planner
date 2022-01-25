package com.olena.guestcleanupservice.enums;

public enum Constants {

    EVENT("event");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
