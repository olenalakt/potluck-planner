package com.olena.drinkservice.enums;

public enum Constants {

    DRINK("drink");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
