package com.olena.guestservice.enums;

public enum GuestStatusEnum {

    // TODO implement status business logic later
    // part of path to  dishes service
    INVITED("INVITED"),
    CONFIRMED("CONFIRMED"),
    CANCELLED("CANCELLED"),
    REMOVED("REMOVED");

    private final String value;

    GuestStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
