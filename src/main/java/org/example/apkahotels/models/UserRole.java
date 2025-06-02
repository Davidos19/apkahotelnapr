package org.example.apkahotels.models;

public enum UserRole {
    GUEST("Guest - może tylko przeglądać"),
    CLIENT("Client - może rezerwować"),
    RECEPTIONIST("Receptionist - może zarządzać rezerwacjami"),
    HOTEL_MANAGER("Hotel Manager - może zarządzać hotelem"),
    ADMIN("Administrator - pełny dostęp");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}