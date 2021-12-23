package com.olena.eventservice.enums;

public enum ActionEnum {

    ADD("I", "ADD"),
    UPDATE("U", "UPDATE"),
    DELETE("D", "DELETE");

    private String code;
    private String name;

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
