package com.olena.userservice.enums;

public enum ActionEnum {

    ADD("I", "ADD"),
    UPDATE("U", "UPDATE"),
    DELETE("D", "DELETE");

    private final String code;
    private final String name;

    ActionEnum(final String id, final String name) {
        this.code = id;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
