package com.example.back.model.dto;

public enum Country {
    UNITED_KINGDOM,
    SPAIN,
    THAILAND;

    public static Country stringToCountry(String s) {
        return switch (s.toLowerCase()) {
            case "united_kingdom" -> UNITED_KINGDOM;
            case "spain" -> SPAIN;
            case "thailand" -> THAILAND;
            default -> null;
        };
    }
}