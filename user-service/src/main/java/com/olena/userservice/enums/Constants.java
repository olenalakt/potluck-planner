package com.olena.userservice.enums;

public enum Constants {

    USER("user");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
