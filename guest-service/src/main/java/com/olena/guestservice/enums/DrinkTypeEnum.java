package com.olena.guestservice.enums;

public enum DrinkTypeEnum {

    // part of path to  dishes service
    WATER("WATER"),
    JUICE("JUICE"),
    WINE("WINE"),
    BEER("BEER"),
    CIDER("CIDER"),
    LIQUEUR("LIQUEUR"),
    TEQUILA("TEQUILA"),
    VODKA("VODKA"),
    WHISKEY("WHISKEY");

    private final String value;

    DrinkTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
