package com.olena.dishservice.enums;

public enum Constants {

    DISH("dish");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
