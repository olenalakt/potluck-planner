package com.olena.menuservice.enums;

public enum Constants {

    MENU("menu");

    private final String value;

    Constants(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
