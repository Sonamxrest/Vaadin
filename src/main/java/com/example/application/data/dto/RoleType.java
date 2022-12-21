package com.example.application.data.dto;

public enum RoleType {
    ADMIN("ADMIN"),
    USER("USER");

    public final String label;

    RoleType(String label) {
        this.label = label;
    }
}
