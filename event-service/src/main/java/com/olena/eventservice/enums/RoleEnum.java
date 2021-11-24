package com.olena.eventservice.enums;

public enum RoleEnum {

    ADMIN("Administrator", "*"),
    USER("EventDTO", "crud");

    private final String name;
    private final String permissions;

    RoleEnum(final String name, final String permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public String getPermissions() {
        return permissions;
    }

    public String getName() {
        return name;
    }

}
