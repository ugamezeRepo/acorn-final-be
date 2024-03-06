package com.acorn.finals.model;

public enum Role {

    ROLE_OWNER("owner"),
    ROLE_MANAGER("manager"),
    ROLE_GUEST("guest"),
    ;

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
