package com.indicaAI.model.roles;

public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");
    String role;
    UserRole(String role){this.role = role;};
}
