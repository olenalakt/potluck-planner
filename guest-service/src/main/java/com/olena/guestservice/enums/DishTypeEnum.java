package com.olena.guestservice.enums;

public enum DishTypeEnum {

    // part of path to  dishes service
    APPETIZER("APPETIZER"),
    SALAD("SALAD"),
    MAIN("MAIN"),
    DESERT("DESERT");

    private final String value;

    DishTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
