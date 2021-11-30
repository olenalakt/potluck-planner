package com.olena.guestservice.enums;

public enum DishType {

    // part of path to  dishes service
    APPETIZER("APPETIZER"),
    SALAD("SALAD"),
    MAIN("MAIN"),
    DESERT("DESERT")
    ;

    private final String value;

    DishType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
