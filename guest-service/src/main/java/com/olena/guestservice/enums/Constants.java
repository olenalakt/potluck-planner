package com.olena.guestservice.enums;

public enum Constants {

    GUEST_SERVICE("guest.service");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
