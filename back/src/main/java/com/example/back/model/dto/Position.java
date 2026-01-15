package com.example.back.model.dto;

public enum Position {
    DIRECTOR,
    COOK,
    CLEANER,
    MANAGER_OF_CLEANING;

    public static Position stringToPosition(String s) {
        return switch (s.toLowerCase()) {
            case "director" -> Position.DIRECTOR;
            case "cook" -> Position.COOK;
            case "cleaner" -> Position.CLEANER;
            case "manager_of_cleaning" -> Position.MANAGER_OF_CLEANING;
            default -> null;
        };
    }
}
